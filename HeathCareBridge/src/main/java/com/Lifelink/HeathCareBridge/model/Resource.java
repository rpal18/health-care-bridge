package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;


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

    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;
    @NotNull(message = "Last updated timestamp is required")
    private LocalDateTime lastUpdated;

    public Resource() {
    }

    public Resource(UUID id, String name, ResourceType resourceType,
                    int quantity, boolean available, Facility facility,
                    LocalDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.resourceType = resourceType;
        this.quantity = quantity;
        this.available = available;
        this.facility = facility;
        this.lastUpdated = lastUpdated;
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
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
