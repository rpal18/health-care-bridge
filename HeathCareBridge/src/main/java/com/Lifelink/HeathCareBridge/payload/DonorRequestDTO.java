package com.Lifelink.HeathCareBridge.payload;

import com.Lifelink.HeathCareBridge.model.BloodComponent;
import com.Lifelink.HeathCareBridge.model.BloodGroup;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DonorRequestDTO extends UserRequestDTO{
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Blood component is required")
    private BloodComponent bloodComponent;
    private String city;

    @Min(18)
    private int age;

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BloodComponent getBloodComponent() {
        return bloodComponent;
    }

    public void setBloodComponent(BloodComponent bloodComponent) {
        this.bloodComponent = bloodComponent;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
