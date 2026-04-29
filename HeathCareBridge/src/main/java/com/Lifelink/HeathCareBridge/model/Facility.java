package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern; 


import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType type;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Facility role is required")
    private FacilityRole facilityRole;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "facility_roles", joinColumns = @JoinColumn(name = "facility_id"))
    @Enumerated(EnumType.STRING)
    private Set<FacilityRole> roles;


    @Column(nullable = false)
    private Boolean directPatientCare;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Facility status is required")
    private FacilityStatus facilityStatus;
    private Boolean is24x7;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
    private String phoneNumber;
    @Email(message = "Invalid email address")
    private String email;

    private double latitude;

    private double longitude;
    private boolean isDeleted = false;
    private LocalDateTime deletedAt ;
    private String deletionReason;

    @Column(nullable = false , updatable = false)
    private LocalDateTime approvedOn;

    @OneToOne(mappedBy = "facility", cascade = CascadeType.ALL)
    private Admin facilityAdmin;

    public Facility() {
    }

    public Facility(UUID id, String name, String address, FacilityType type,
                    FacilityRole facilityRole, Set<FacilityRole> roles,
                    Boolean directPatientCare, Boolean is24x7, String phoneNumber,
                    String email, double latitude, double longitude) {
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

    public Set<FacilityRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<FacilityRole> roles) {
        this.roles = roles;
    }

    public boolean isDirectPatientCare() {
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

    public FacilityRole getFacilityRole() {
        return facilityRole;
    }

    public void setFacilityRole(FacilityRole facilityRole) {
        this.facilityRole = facilityRole;
    }

    public Boolean getDirectPatientCare() {
        return directPatientCare;
    }

    public FacilityStatus getFacilityStatus() {
        return facilityStatus;
    }

    public void setFacilityStatus(FacilityStatus facilityStatus) {
        this.facilityStatus = facilityStatus;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletionReason() {
        return deletionReason;
    }

    public void setDeletionReason(String deletionReason) {
        this.deletionReason = deletionReason;
    }

    public Admin getFacilityAdmin() {
        return facilityAdmin;
    }

    public void setFacilityAdmin(Admin facilityAdmin) {
        this.facilityAdmin = facilityAdmin;
    }

    public LocalDateTime getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(LocalDateTime approvedOn) {
        this.approvedOn = approvedOn;
    }
}
