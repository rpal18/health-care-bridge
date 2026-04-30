package com.Lifelink.HeathCareBridge.payload;

import com.Lifelink.HeathCareBridge.model.Facility;
import com.Lifelink.HeathCareBridge.model.FacilityRole;
import com.Lifelink.HeathCareBridge.model.ResourceType;

import java.time.LocalDateTime;
import java.util.UUID;

public class BloodResourceResponseDTO extends ResourceResponseDTO{
    private String bloodGroup;

    private String bloodComponent;

    public BloodResourceResponseDTO() {
    }

    public BloodResourceResponseDTO(UUID id, String name, ResourceType resourceType, int quantity,
                                    Facility facility, boolean available, String bloodGroup,
                                    String bloodComponent , FacilityRole facilityRole , LocalDateTime lastUpdated) {
        super(id, name, resourceType, quantity, facility, available , lastUpdated,facilityRole);
        this.bloodGroup = bloodGroup;
        this.bloodComponent = bloodComponent;
    }

    public BloodResourceResponseDTO(String bloodGroup, String bloodComponent) {
        this.bloodGroup = bloodGroup;
        this.bloodComponent = bloodComponent;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBloodComponent() {
        return bloodComponent;
    }

    public void setBloodComponent(String bloodComponent) {
        this.bloodComponent = bloodComponent;
    }
}
