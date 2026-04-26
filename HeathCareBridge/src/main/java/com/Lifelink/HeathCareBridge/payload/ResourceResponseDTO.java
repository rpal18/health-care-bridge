package com.Lifelink.HeathCareBridge.payload;

import com.Lifelink.HeathCareBridge.model.Facility;
import com.Lifelink.HeathCareBridge.model.FacilityRole;
import com.Lifelink.HeathCareBridge.model.FacilityType;
import com.Lifelink.HeathCareBridge.model.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


public class ResourceResponseDTO {
    private UUID id;
    private String name;
    private ResourceType resourceType;
    private int quantity;
    private Facility facility;
    private boolean available;

    private LocalDateTime lastUpdated;

    private FacilityRole facilityRole;

    public ResourceResponseDTO(UUID id, String name, ResourceType resourceType,
                               int quantity, Facility facility, boolean available
    , LocalDateTime lastUpdated , FacilityRole facilityRole) {
        this.id = id;
        this.name = name;
        this.resourceType = resourceType;
        this.quantity = quantity;
        this.facility = facility;
        this.available = available;
        this.lastUpdated = lastUpdated;
        this.facilityRole = facilityRole;
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
}
