package com.Lifelink.HeathCareBridge.payload;

import com.Lifelink.HeathCareBridge.model.FacilityRole;
import com.Lifelink.HeathCareBridge.model.FacilityType;
import com.Lifelink.HeathCareBridge.model.ResourceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public class ResourceDTO {
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Resource type is required")
    private ResourceType resourceType;
    @NotBlank(message = "Resource name is required")
    private String name;
    @Min(value = 0, message = "Quantity must be non-negative")
    private int quantity;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Facility type is required")
    private FacilityType facilityType;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Facility role is required")
    private FacilityRole facilityRole;
    @NotBlank(message = "Facility name is required")
    private String facilityName;

    public ResourceDTO() {
    }

    public ResourceDTO(ResourceType resourceType, String name, int quantity,
                       FacilityType facilityType, String facilityName , FacilityRole facilityRole) {
        this.resourceType = resourceType;
        this.name = name;
        this.quantity = quantity;
        this.facilityType = facilityType;
        this.facilityName = facilityName;
        this.facilityRole = facilityRole;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public FacilityRole getFacilityRole() {
        return facilityRole;
    }

    public void setFacilityRole(FacilityRole facilityRole) {
        this.facilityRole = facilityRole;
    }
}
