package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class RequestedFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID requestedFacilityId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityStatus facilityStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityRole facilityRole;

    @Column(nullable = false)
    private Boolean directPatientCare;
    @Column(nullable = false)
    private Boolean is24x7;


    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
    private String phoneNumber;

    @Email(message = "Invalid email address")
    private String email;

    @Column(nullable = false)
    private UUID facilityRequesterId;

    private double latitude;
    private double longitude;

    private LocalDateTime requestedAt;

    public RequestedFacility() {}


    public RequestedFacility(UUID id, String name, String address, FacilityType type,
                             FacilityStatus facilityStatus, FacilityRole facilityRole,
                             Boolean directPatientCare, Boolean is24x7, String phoneNumber,
                             String email, double latitude, double longitude, LocalDateTime requestedAt
    , UUID facilityRequesterId) {
        this.requestedFacilityId = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.facilityStatus = facilityStatus;
        this.facilityRole = facilityRole;
        this.directPatientCare = directPatientCare;
        this.is24x7 = is24x7;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.requestedAt = requestedAt;
        this.facilityRequesterId = facilityRequesterId;
    }

    public UUID getId() {
        return requestedFacilityId;
    }

    public void setId(UUID id) {
        this.requestedFacilityId = id;
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

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public FacilityStatus getFacilityStatus() {
        return facilityStatus;
    }

    public void setFacilityStatus(FacilityStatus facilityStatus) {
        this.facilityStatus = facilityStatus;
    }

    public FacilityRole getFacilityRole() {
        return facilityRole;
    }

    public void setFacilityRole(FacilityRole facilityRole) {
        this.facilityRole = facilityRole;
    }

    public Boolean isDirectPatientCare() {
        return directPatientCare;
    }

    public void setDirectPatientCare(Boolean directPatientCare) {
        this.directPatientCare = directPatientCare;
    }

    public Boolean getIs24x7() {
        return is24x7;
    }

    public void setIs24x7(Boolean is24x7) {
        this.is24x7 = is24x7;
    }

    public UUID getRequestedFacilityId() {
        return requestedFacilityId;
    }

    public void setRequestedFacilityId(UUID requestedFacilityId) {
        this.requestedFacilityId = requestedFacilityId;
    }

    public Boolean getDirectPatientCare() {
        return directPatientCare;
    }

    public UUID getFacilityRequesterId() {
        return facilityRequesterId;
    }

    public void setFacilityRequesterId(UUID facilityRequesterId) {
        this.facilityRequesterId = facilityRequesterId;
    }
}