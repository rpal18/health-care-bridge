package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.payload.AiResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AiService {
    private final ChatClient chatClient;
    @Autowired
    public AiService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }

    public AiResponse analyzeEmergency(String userDescription , MultipartFile imageFile) {
        String systemPrompt = """
                You are an expert emergency medical triage AI.
                Analyze the provided image (if any) and patient description.
                            
                Based on the trauma or symptoms, deduce the REQUIRED RESOURCES from the exact Enum values provided in the schema.
                - If there is severe trauma, bleeding, or breathing issues, prioritize ICU_BED, VENTILATOR, or OXYGEN.
                - If the user explicitly mentions needing blood, or has massive blood loss, include BLOOD in requiredResources.
                - Only populate bloodGroup and bloodComponent if the user explicitly mentions their blood type or specific component needs in the text. Otherwise, leave them null.
                - Output severityLevel as either LOW, MEDIUM, or HIGH.
                - Provide a brief 1-sentence clinicalReasoning.
                """;

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userSpec -> {
                    userSpec.text("Patient Description: " + (userDescription != null ?
                            userDescription : "No description provided. Rely on image."));
                    if (imageFile != null && !imageFile.isEmpty()) {
                        try {
                            userSpec.media(MimeTypeUtils.parseMimeType(imageFile.getContentType()),
                                    new ByteArrayResource(imageFile.getBytes()));
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to process image", e);
                        }
                    }
                })
                .call()
                .entity(AiResponse.class);

    }
}
