package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {

    Resource findByNameAndFacilityNameAndFacilityTypeAndResourceType(
            String name, String facilityName, FacilityType type, ResourceType resourceType);

    @Query(value = "SELECT DISTINCT r.facility_name FROM resource r " +
            "WHERE r.resource_type IN :resourcesRequired " +
            "AND r.available = true " +
            "ORDER BY r.location <-> ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) ASC " +
            "LIMIT 10", nativeQuery = true)
    List<Facility> findTop10NearestFacilitiesWithResources(@Param("resourcesRequired") List<ResourceType> resourcesRequired,
                                                         @Param("longitude") Double longitude,
                                                         @Param("latitude") Double latitude);

    @Query(value = "SELECT DISTINCT r.facility_name FROM blood r " +
            "WHERE r.resource_type IN :resourcesRequired " +
            "AND r.available = true " +
            "AND r.blood_group = :bloodGroup " +
            "AND r.blood_component = :bloodComponent " +
            "ORDER BY r.location <-> ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) ASC " +
            "LIMIT 10", nativeQuery = true)
    List<Facility> findTop10NearestFacilitiesWithBloodResources(@Param("resourcesRequired") List<ResourceType> resourcesRequired,
                                                              @Param("longitude") Double longitude,
                                                              @Param("latitude") Double latitude,
                                                              @Param("bloodGroup") BloodGroup bloodGroup,
                                                              @Param("bloodComponent") BloodComponent bloodComponent);
    }
