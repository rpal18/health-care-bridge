package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "donors")
@DiscriminatorValue("DONOR")
public class Donor extends User {
    private String bloodType;
    private final Role role = Role.DONOR;

    public Donor() {
    }

    public Donor(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}