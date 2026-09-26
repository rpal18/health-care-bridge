package com.Lifelink.HeathCareBridge.ai.web;

import com.Lifelink.HeathCareBridge.ai.model.EmergencyResponse;
import com.Lifelink.HeathCareBridge.ai.service.GeminiMultiModelService;
import com.Lifelink.HeathCareBridge.exceptions.IllegalArgument;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/api/ai")
public class GeminiController {

    private final GeminiMultiModelService geminiMultiModelService;

    public GeminiController(GeminiMultiModelService geminiMultiModelService) {
        this.geminiMultiModelService = geminiMultiModelService;
    }

    @PostMapping(value = "/nearby-help", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<EmergencyResponse> resolution(
            @RequestParam(value = "file", required = false) MultipartFile audioFile,
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude
    ) throws IOException {

        boolean hasAudio = audioFile != null && !audioFile.isEmpty();
        boolean hasImage = imageFile != null && !imageFile.isEmpty();
        boolean hasText = text != null && !text.isBlank();

        if (!hasAudio && !hasImage && !hasText) {
            throw new IllegalArgument("Provide at least one of: text, audio file, or image file.");
        }

        String audioMimeType = null;
        byte[] audioBytes = null;
        if (hasAudio) {
            audioMimeType = audioFile.getContentType();
            if (audioMimeType == null || !audioMimeType.startsWith("audio/")) {
                throw new IllegalArgumentException("Expected an audio file, got: " + audioMimeType);
            }
            audioBytes = audioFile.getBytes();
        }

        String imageMimeType = null;
        byte[] imageBytes = null;
        if (hasImage) {
            imageMimeType = imageFile.getContentType();
            if (imageMimeType == null || !imageMimeType.startsWith("image/")) {
                throw new IllegalArgumentException("Expected an image file, got: " + imageMimeType);
            }
            imageBytes = imageFile.getBytes();
        }

        return geminiMultiModelService.handleEmergency(
                text, audioBytes, audioMimeType, imageBytes, imageMimeType, latitude, longitude);
    }

}