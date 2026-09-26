package com.Lifelink.HeathCareBridge.ai.service;

import com.Lifelink.HeathCareBridge.ai.model.*;
import com.Lifelink.HeathCareBridge.projection.FacilityLocationProjection;
import com.Lifelink.HeathCareBridge.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class GeminiMultiModelService {

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    private final ResourceRepository resourceRepository;
    private ResourceRequired resourceRequired;

    public GeminiMultiModelService(@Value("${gemini.api.key}") String apiKey , ObjectMapper objectMapper , ResourceRepository resourceRepository) {
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader("x-goog-api-key", apiKey)
                .build();

        this.objectMapper = objectMapper;
        this.resourceRepository = resourceRepository;
    }

    public Mono<String> askText(String prompt) {
        return generate(List.of(Map.of("text", prompt)));
    }

    public Mono<String> askAboutAudio(byte[] audioBytes, String mimeType, String prompt) {
        String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
        return generate(List.of(
                Map.of("text", prompt),
                Map.of("inline_data", Map.of("mime_type", mimeType, "data", base64Audio))
        ));
    }

    private Mono<String> generate(List<Map<String, Object>> parts) {
        Map<String, Object> body = Map.of(
                "contents", new Object[]{ Map.of("parts", parts) }
        );

        return webClient.post()
                .uri("/models/gemini-3.1-flash-lite:generateContent")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .retryWhen(reactor.util.retry.Retry.backoff(3, java.time.Duration.ofSeconds(2))
                        .filter(ex -> ex instanceof WebClientResponseException wcre
                                && wcre.getStatusCode().value() == 503))
                .map(this::extractText);
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> response) {
        var candidates = (List<Map<String, Object>>) response.get("candidates");
        var content = (Map<String, Object>) candidates.get(0).get("content");
        var parts = (List<Map<String, Object>>) content.get("parts");
        return (String) parts.get(0).get("text");
    }

    private Mono<EmergencyAssessment> assessEmergency(
            String description, byte[] audioBytes, String audioMimeType,
            byte[] imageBytes, String imageMimeType) {

        List<Object> parts = new ArrayList<>();
        parts.add(Map.of("text", instructionPrompt()));

        if (description != null && !description.isBlank()) {
            parts.add(Map.of("text", "Patient description: " + description));
        }
        if (audioBytes != null && audioBytes.length > 0) {
            parts.add(Map.of("inline_data", Map.of(
                    "mime_type", audioMimeType, "data", Base64.getEncoder().encodeToString(audioBytes))));
        }
        if (imageBytes != null && imageBytes.length > 0) {
            parts.add(Map.of("inline_data", Map.of(
                    "mime_type", imageMimeType, "data", Base64.getEncoder().encodeToString(imageBytes))));
        }

        if (parts.size() == 1) {
            return Mono.error(new IllegalArgumentException("Provide at least one of: description, audio, or image."));
        }

        Map<String, Object> body = Map.of(
                "contents", new Object[]{ Map.of("parts", parts) },
                "generationConfig", Map.of(
                        "responseMimeType", "application/json",
                        "responseSchema", GeminiSchema.emergencyAssessmentSchema()
                )
        );

        return webClient.post()
                .uri("/models/gemini-3.1-flash-lite:generateContent")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(2))
                        .filter(ex -> ex instanceof WebClientResponseException wcre && wcre.getStatusCode().value() == 503))
                .map(this::extractRawJson)
                .map(this::parseAssessment);
    }

    @SuppressWarnings("unchecked")
    private String extractRawJson(Map<String, Object> response) {
        var candidates = (List<Map<String, Object>>) response.get("candidates");
        var content = (Map<String, Object>) candidates.get(0).get("content");
        var parts = (List<Map<String, Object>>) content.get("parts");
        return (String) parts.get(0).get("text");
    }

    private EmergencyAssessment parseAssessment(String json) {
        try {
            return objectMapper.readValue(json, EmergencyAssessment.class);
        } catch (Exception e) {
            throw new IllegalStateException("Gemini returned malformed assessment JSON", e);
        }
    }

    private String instructionPrompt(){
        String systemPrompt = """
                You are a triage support assistant for an emergency healthcare response system.
                Analyze the patient's description and:
                1. Assess severity: CRITICAL, URGENT, MODERATE, or LOW.
                2. Give a one-sentence plain-language summary of the situation.
                3. List the specific resources needed, choosing only from the allowed resource types.
                If a genuinely needed resource doesn't fit the list, use OTHER and explain why in "reason".
                Do not inflate quantities beyond what's reasonable for one patient unless the input implies otherwise.
                This output supports a human responder's decision. It does not replace clinical judgment.
                For any resource with resourceType BLOOD, you must also specify bloodGroup and bloodComponent
                from the allowed values. For every other resourceType, omit both fields entirely.
                """;
        return systemPrompt;
    }

    public Mono<EmergencyResponse> handleEmergency(
            String description, byte[] audioBytes, String audioMimeType,
            byte[] imageBytes, String imageMimeType, double latitude, double longitude) {

        return assessEmergency(description, audioBytes, audioMimeType, imageBytes, imageMimeType)
                .flatMap(assessment -> lookupFacilities(assessment, latitude, longitude)
                        .map(result -> new EmergencyResponse(
                                assessment,
                                result.general() , result.blood()
                        )));
    }

    private Mono<FacilityLookupResult> lookupFacilities(EmergencyAssessment assessment, double lat, double lon) {
        return Mono.fromCallable(() -> {
            List<String> nonBloodTypes = assessment.resources().stream()
                    .map(ResourceRequired::resourceType)
                    .filter(type -> !type.equals("BLOOD"))
                    .distinct()
                    .toList();

            List<FacilityResult> general = nonBloodTypes.isEmpty()
                    ? List.of()
                    : resourceRepository.findTop10NearestFacilityLocations(nonBloodTypes, lon, lat).stream().map(FacilityResult::from).toList();

            List<FacilityLocationProjection> blood = assessment.resources().stream()
                    .filter(r -> r.resourceType().equals("BLOOD"))
                    .findFirst()
                    .filter(r -> r.bloodGroup() != null && r.bloodComponent() != null) // guard against a malformed/incomplete AI response
                    .map(r -> resourceRepository.findTop10NearestBloodFacilityLocations(
                            List.of("BLOOD"), lon, lat, r.bloodGroup(), r.bloodComponent()))
                    .orElse(List.of());

            List<FacilityResult> bloodResults = blood.stream().map(FacilityResult :: from).toList();

            return new FacilityLookupResult(general, bloodResults);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}