package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
}