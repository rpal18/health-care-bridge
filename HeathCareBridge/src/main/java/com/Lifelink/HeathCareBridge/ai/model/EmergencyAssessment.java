package com.Lifelink.HeathCareBridge.ai.model;

import java.util.List;

public record EmergencyAssessment(
        String severityLevel,
        String summary,
        List<ResourceRequired> resources
) {}