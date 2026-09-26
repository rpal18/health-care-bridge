
package com.Lifelink.HeathCareBridge.ai.model;

import com.Lifelink.HeathCareBridge.model.BloodComponent;
import com.Lifelink.HeathCareBridge.model.BloodGroup;
import com.Lifelink.HeathCareBridge.model.ResourceType; // adjust to your real package

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GeminiSchema {

    public static Map<String, Object> emergencyAssessmentSchema() {
        Map<String, Object> resourceTypeSchema = Map.of(
                "type", "STRING",
                "enum", Arrays.stream(ResourceType.values()).map(Enum::name).toList()
        );

        Map<String, Object> bloodGroupSchema = Map.of(
                "type", "STRING",
                "enum", Arrays.stream(BloodGroup.values()).map(Enum::name).toList()
        );

        Map<String, Object> bloodComponentSchema = Map.of(
                "type", "STRING",
                "enum", Arrays.stream(BloodComponent.values()).map(Enum::name).toList()
        );

        Map<String, Object> resourceItemSchema = Map.of(
                "type", "OBJECT",
                "properties", Map.of(
                        "resourceType", resourceTypeSchema,
                        "quantity", Map.of("type", "INTEGER"),
                        "reason", Map.of("type", "STRING"),
                        "bloodGroup", bloodGroupSchema,
                        "bloodComponent", bloodComponentSchema
                ),
                // note: bloodGroup / bloodComponent deliberately NOT in "required" —
                // the model can omit them for every resourceType except BLOOD
                "required", List.of("resourceType", "quantity", "reason")
        );

        return Map.of(
                "type", "OBJECT",
                "properties", Map.of(
                        "severityLevel", Map.of("type", "STRING", "enum", List.of("CRITICAL", "URGENT", "MODERATE", "LOW")),
                        "summary", Map.of("type", "STRING"),
                        "resources", Map.of("type", "ARRAY", "items", resourceItemSchema)
                ),
                "required", List.of("severityLevel", "summary", "resources")
        );
    }
}