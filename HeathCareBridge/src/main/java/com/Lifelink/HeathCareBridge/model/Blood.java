package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@DiscriminatorValue("BLOOD")
public class Blood extends Resource{
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)

    private BloodComponent bloodComponent;

    public Blood() {
    }

    public Blood(UUID id, String name, ResourceType resourceType, int quantity, boolean available, FacilityType facilityType, String facilityName, LocalDateTime lastUpdated) {
        super(id, name, resourceType, quantity, available, facilityType, facilityName, lastUpdated);
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public BloodComponent getBloodComponent() {
        return bloodComponent;
    }

    public void setBloodComponent(BloodComponent bloodComponent) {
        this.bloodComponent = bloodComponent;
    }
}
