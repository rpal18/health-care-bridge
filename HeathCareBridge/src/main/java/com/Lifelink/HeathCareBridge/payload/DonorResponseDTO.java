package com.Lifelink.HeathCareBridge.payload;

import com.Lifelink.HeathCareBridge.model.BloodComponent;
import com.Lifelink.HeathCareBridge.model.BloodGroup;

public class DonorResponseDTO extends UserResponseDTO{
    private BloodGroup bloodGroup;
    private BloodComponent bloodComponent;
    private int units;

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

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
