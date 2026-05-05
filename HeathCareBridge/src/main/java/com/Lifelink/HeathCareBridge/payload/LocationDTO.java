package com.Lifelink.HeathCareBridge.payload;

public class LocationDTO {
    private Double latitude;
    private Double longitude;

    public LocationDTO() {
    }

    public LocationDTO(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
