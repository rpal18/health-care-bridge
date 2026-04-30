package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.Blood;
import com.Lifelink.HeathCareBridge.model.BloodComponent;
import com.Lifelink.HeathCareBridge.model.FacilityType;
import com.Lifelink.HeathCareBridge.model.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BloodRepository extends JpaRepository<Blood, UUID> {
    Blood findByNameAndFacilityNameAndFacilityTypeAndResourceType(
            String name, String name1, FacilityType type, ResourceType resourceType);

    Blood findByNameAndFacilityNameAndFacilityTypeAndResourceTypeAndBloodComponent(
            String name, String name1, FacilityType type, ResourceType resourceType, BloodComponent bloodComponent);
}