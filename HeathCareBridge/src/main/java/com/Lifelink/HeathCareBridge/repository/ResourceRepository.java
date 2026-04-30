package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.BloodComponent;
import com.Lifelink.HeathCareBridge.model.FacilityType;
import com.Lifelink.HeathCareBridge.model.Resource;
import com.Lifelink.HeathCareBridge.model.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {

    Resource findByNameAndFacilityNameAndFacilityTypeAndResourceType(
            String name, String facilityName, FacilityType type, ResourceType resourceType);

}