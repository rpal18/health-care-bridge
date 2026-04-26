package com.Lifelink.HeathCareBridge.payload;

import com.Lifelink.HeathCareBridge.model.FacilityRole;
import com.Lifelink.HeathCareBridge.model.FacilityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public class FacilityDTO {

    @NotBlank(message = "Facility name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Facility type is required")
    private FacilityType type;

    @NotNull(message = "Facility role is required")
    private FacilityRole facilityRole;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Set<FacilityRole> roles;
    @NotNull(message = "Direct patient care information is required")
    private Boolean directPatientCare;
    @NotNull(message = "24x7 operation information is required")
    private Boolean is24x7;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
    private String phoneNumber;

    @Email(message = "Invalid email address")
    private String email;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;

    public FacilityDTO() {
    }

    public FacilityDTO(String name, String address, FacilityType type, FacilityRole facilityRole,
                       Set<FacilityRole> roles, boolean directPatientCare, Boolean is24x7,
                       String phoneNumber, String email, double latitude, double longitude) {
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
}