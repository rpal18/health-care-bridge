package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;

@Entity
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "facility_roles", joinColumns = @JoinColumn(name = "facility_id"))
    @Enumerated(EnumType.STRING)
    private Set<FacilityRole> roles;


    @Column(nullable = false)
    private boolean directPatientCare;


    private Boolean is24x7;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
    private String phoneNumber;
    @Email(message = "Invalid email address")
    private String email;

    private double latitude;

    private double longitude;

    public Facility() {
    }

    public Facility(Long id, String name, String address, FacilityType type,
                    Set<FacilityRole> roles, boolean directPatientCare, Boolean is24x7,
                    String phoneNumber, String email, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.roles = roles;
        this.directPatientCare = directPatientCare;
        this.is24x7 = is24x7;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
