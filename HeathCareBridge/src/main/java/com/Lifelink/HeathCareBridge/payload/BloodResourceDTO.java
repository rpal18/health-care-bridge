package com.Lifelink.HeathCareBridge.payload;

import com.Lifelink.HeathCareBridge.model.BloodComponent;
import com.Lifelink.HeathCareBridge.model.BloodGroup;
import com.Lifelink.HeathCareBridge.model.FacilityType;
import com.Lifelink.HeathCareBridge.model.ResourceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public class BloodResourceDTO extends ResourceDTO{
    @Enumerated(EnumType.STRING)
    @NotNull
    private BloodGroup bloodGroup;
    @Enumerated(EnumType.STRING)
    @NotNull
    private BloodComponent bloodComponent;

    public BloodResourceDTO(BloodGroup bloodGroup, BloodComponent bloodComponent) {
        this.bloodGroup = bloodGroup;
        this.bloodComponent = bloodComponent;
    }

    public BloodResourceDTO(ResourceType resourceType, String name, int quantity, FacilityType facilityType, String facilityName, BloodGroup bloodGroup, BloodComponent bloodComponent) {
        super(resourceType, name, quantity, facilityType, facilityName);
        this.bloodGroup = bloodGroup;
        this.bloodComponent = bloodComponent;
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
