package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.Facility;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FacilityRepository extends JpaRepository<Facility, UUID> {

    Facility findFacilityByPhoneNumberOrEmail(String phoneNumber, String email);
}