package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.RequestedFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestedFacilityRepository extends JpaRepository<RequestedFacility, UUID> {
    RequestedFacility findByPhoneNumberOrEmail(String phoneNumber, String email);

}