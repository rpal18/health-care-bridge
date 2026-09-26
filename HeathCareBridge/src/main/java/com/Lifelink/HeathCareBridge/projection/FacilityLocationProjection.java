package com.Lifelink.HeathCareBridge.projection;
public interface FacilityLocationProjection {
    String getFacilityName();
    Double getLongitude();
    Double getLatitude();
    Double getDistance();

    String getMapLink();

    default String mapLink(){
        return "https://www.google.com/maps/search/?api=1&query=" + getLatitude() + "," + getLongitude();
    }
}
