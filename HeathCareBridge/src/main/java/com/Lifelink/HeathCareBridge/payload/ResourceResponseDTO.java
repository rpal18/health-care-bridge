package com.Lifelink.HeathCareBridge.payload;

import com.Lifelink.HeathCareBridge.model.Facility;
import com.Lifelink.HeathCareBridge.model.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


public class ResourceResponseDTO {
    private UUID id;
    private String name;
    private ResourceType resourceType;
    private int quantity;
    private Facility facility;
    private boolean available;

    public ResourceResponseDTO(UUID id, String name, ResourceType resourceType, int quantity, Facility facility, boolean available) {
        this.id = id;
        this.name = name;
        this.resourceType = resourceType;
        this.quantity = quantity;
        this.facility = facility;
        this.available = available;
    }

    public ResourceResponseDTO() {
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

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
