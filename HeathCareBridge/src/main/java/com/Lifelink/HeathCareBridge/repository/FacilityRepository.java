package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacilityRepository extends JpaRepository<Facility, UUID> {

    Facility findFacilityByPhoneNumberOrEmail(String phoneNumber, String email);

    Optional<Facility> findByIdAndIsDeletedFalse(UUID facilityId);

    List<Facility> findAllByIsDeletedFalse();
}