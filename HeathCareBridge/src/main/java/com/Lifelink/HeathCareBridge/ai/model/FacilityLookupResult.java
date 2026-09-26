package com.Lifelink.HeathCareBridge.ai.model;

import com.Lifelink.HeathCareBridge.projection.FacilityLocationProjection;

import java.util.List;

public record FacilityLookupResult(
        List<FacilityResult> general,
        List<FacilityResult> blood
) {}