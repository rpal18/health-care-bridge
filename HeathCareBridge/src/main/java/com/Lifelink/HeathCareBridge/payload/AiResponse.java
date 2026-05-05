package com.Lifelink.HeathCareBridge.payload;

import com.Lifelink.HeathCareBridge.model.BloodComponent;
import com.Lifelink.HeathCareBridge.model.BloodGroup;
import com.Lifelink.HeathCareBridge.model.ResourceType;

import java.util.List;

public record AiResponse(
        String severityLevel,
        List<ResourceType> requiredResources,
        BloodGroup bloodGroup,
        BloodComponent bloodComponent,
        String clinicalReasoning
) {
}
