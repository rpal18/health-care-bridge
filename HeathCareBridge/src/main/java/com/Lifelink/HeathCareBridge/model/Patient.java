package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "patients")
@DiscriminatorValue("PATIENT")
public class Patient extends User {
    private String medicalHistory;
    private final Role role = Role.PATIENT;

    public Patient() {
    }


    public Patient(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public Patient(UUID id, String name, String email, String password, String phoneNumber, Set<Role> roles, String medicalHistory) {
        super(id, name, email, password, phoneNumber, roles);
        this.medicalHistory = medicalHistory;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}