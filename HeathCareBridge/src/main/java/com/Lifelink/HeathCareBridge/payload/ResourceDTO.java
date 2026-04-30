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

    public ResourceDTO() {
    }

    public ResourceDTO(ResourceType resourceType, String name, int quantity) {
        this.resourceType = resourceType;
        this.name = name;
        this.quantity = quantity;
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
}
