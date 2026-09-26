package com.Lifelink.HeathCareBridge.ai.model;

import com.Lifelink.HeathCareBridge.projection.FacilityLocationProjection;

import java.util.List;

public record EmergencyResponse(
        EmergencyAssessment assessment,
        List<FacilityResult> generalFacilities,
        List<FacilityResult> bloodFacilities

) {
}
