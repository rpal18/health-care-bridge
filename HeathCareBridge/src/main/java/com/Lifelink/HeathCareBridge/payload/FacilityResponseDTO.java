package com.Lifelink.HeathCareBridge.payload;

import com.Lifelink.HeathCareBridge.model.FacilityRole;
import com.Lifelink.HeathCareBridge.model.FacilityStatus;
import com.Lifelink.HeathCareBridge.model.FacilityType;

import java.util.Set;
import java.util.UUID;

public class FacilityResponseDTO {

    private UUID id;

    private String name;

    private String address;

    private FacilityType type;

    private FacilityRole facilityRole;

    private Set<FacilityRole> roles;

    private boolean directPatientCare;

    private Boolean is24x7;

    private String phoneNumber;

    private String email;

    private double latitude;

    private double longitude;

    private FacilityStatus facilityStatus;

    // Constructor


    public FacilityResponseDTO() {
    }

    public FacilityResponseDTO(UUID id, String name, String address, FacilityType type, FacilityRole facilityRole,
                               Set<FacilityRole> roles, boolean directPatientCare, Boolean is24x7, String phoneNumber,
                               String email, double latitude, double longitude , FacilityStatus facilityStatus) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.facilityRole = facilityRole;
        this.roles = roles;
        this.directPatientCare = directPatientCare;
        this.is24x7 = is24x7;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.facilityStatus = facilityStatus;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public FacilityType getType() {
        return type;
    }

    public void setType(FacilityType type) {
        this.type = type;
    }

    public FacilityRole getFacilityRole() {
        return facilityRole;
    }

    public void setFacilityRole(FacilityRole facilityRole) {
        this.facilityRole = facilityRole;
    }

    public Set<FacilityRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<FacilityRole> roles) {
        this.roles = roles;
    }

    public boolean isDirectPatientCare() {
        return directPatientCare;
    }

    public void setDirectPatientCare(boolean directPatientCare) {
        this.directPatientCare = directPatientCare;
    }

    public Boolean getIs24x7() {
        return is24x7;
    }

    public void setIs24x7(Boolean is24x7) {
        this.is24x7 = is24x7;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public FacilityStatus getFacilityStatus() {
        return facilityStatus;
    }

    public void setFacilityStatus(FacilityStatus facilityStatus) {
        this.facilityStatus = facilityStatus;
    }
}