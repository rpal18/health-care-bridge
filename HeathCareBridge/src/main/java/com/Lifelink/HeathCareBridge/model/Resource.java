package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING)
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank(message = "Resource name is required")
    private String name;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Resource type is required")
    private ResourceType resourceType;

    private int quantity;

    private boolean available;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Facility type is required")
    private FacilityType facilityType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Facility role is required")
    private FacilityRole facilityRole;
    @NotBlank(message = "Facility name is required")
    private String facilityName;
    @NotNull(message = "Last updated timestamp is required")
    private LocalDateTime lastUpdated;
    @NotBlank(message = "please add facility phone number")
    private String facilityPhoneNumber;
    @NotBlank(message = " please add facility email")
    private String facilityEmail;

    // constructors



    public Resource() {
    }

    public Resource(UUID id, String name, ResourceType resourceType,
                    int quantity, boolean available, FacilityType facilityType,
                    String facilityName, LocalDateTime lastUpdated , String facilityPhoneNumber , String facilityEmail) {
        this.id = id;
        this.name = name;
        this.resourceType = resourceType;
        this.quantity = quantity;
        this.available = available;
        this.facilityType = facilityType;
        this.facilityName = facilityName;
        this.lastUpdated = lastUpdated;
        this.facilityEmail = facilityEmail;
        this.facilityPhoneNumber = facilityPhoneNumber;
    }

    // getters and setters
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

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public FacilityRole getFacilityRole() {
        return facilityRole;
    }

    public void setFacilityRole(FacilityRole facilityRole) {
        this.facilityRole = facilityRole;
    }

    public String getFacilityPhoneNumber() {
        return facilityPhoneNumber;
    }

    public void setFacilityPhoneNumber(String facilityPhoneNumber) {
        this.facilityPhoneNumber = facilityPhoneNumber;
    }

    public String getFacilityEmail() {
        return facilityEmail;
    }

    public void setFacilityEmail(String facilityEmail) {
        this.facilityEmail = facilityEmail;
    }
}
