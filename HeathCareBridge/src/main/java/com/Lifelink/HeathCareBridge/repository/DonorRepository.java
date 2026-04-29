package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DonorRepository extends JpaRepository<Donor, UUID> {
    Donor findDonorByEmailOrPhoneNumber(String email, String phoneNumber);
}