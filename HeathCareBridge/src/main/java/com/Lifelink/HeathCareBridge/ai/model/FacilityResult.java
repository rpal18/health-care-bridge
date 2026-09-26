package com.Lifelink.HeathCareBridge.ai.model;

import com.Lifelink.HeathCareBridge.projection.FacilityLocationProjection;

public record FacilityResult(
        String facilityName,
        Double latitude,
        Double longitude,
        Double distance,
        String mapLink
) {
    public static FacilityResult from(FacilityLocationProjection projection) {
        return new FacilityResult(
                projection.getFacilityName(),
                projection.getLatitude(),
                projection.getLongitude(),
                projection.getDistance(),
                buildMapLink(projection.getLatitude(), projection.getLongitude())
        );
    }

    private static String buildMapLink(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return null;
        }
        return String.format("https://www.google.com/maps/search/?api=1&query=%s,%s", latitude, longitude);
    }
}