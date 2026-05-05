package com.Lifelink.HeathCareBridge.payload;

public class NearByResponseDTO {
    private String facilityName;
    private   LocationDTO location;
    private Double distance;

    public NearByResponseDTO() {
    }

    public NearByResponseDTO(String facilityName, LocationDTO location, Double distance) {
        this.facilityName = facilityName;
        this.location = location;
        this.distance = distance;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
