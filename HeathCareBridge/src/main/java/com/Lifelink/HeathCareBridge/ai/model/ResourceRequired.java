package com.Lifelink.HeathCareBridge.ai.model;



public record ResourceRequired(
        String resourceType  ,
        int quantity,
        String reason ,
        String bloodGroup ,
        String bloodComponent

) {
}

