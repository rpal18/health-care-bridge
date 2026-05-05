package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.*;
import com.Lifelink.HeathCareBridge.projection.FacilityLocationProjection;
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

    @Query(value = "SELECT r.facility_name AS facilityName, " +
            "ST_Y(r.location::geometry) AS latitude, " +
            "ST_X(r.location::geometry) AS longitude, " +
            "ST_Distance(r.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) AS distance " +
            "FROM resource r " +
            "WHERE r.resource_type IN :resourcesRequired " +
            "AND r.available = true " +
            "GROUP BY r.facility_name, r.location " +
            "ORDER BY distance ASC " +
            "LIMIT 10", nativeQuery = true)
    List<FacilityLocationProjection> findTop10NearestFacilityLocations(@Param("resourcesRequired") List<String> resourcesRequired,
                                                                       @Param("longitude") Double longitude,
                                                                       @Param("latitude") Double latitude);

    @Query(value = "SELECT r.facility_name AS facilityName, " +
            "ST_Y(r.location::geometry) AS latitude, " +
            "ST_X(r.location::geometry) AS longitude, " +
            "ST_Distance(r.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) AS distance " +
            "FROM resource r " +
            "WHERE r.resource_type IN :resourcesRequired " +
            "AND r.available = true " +
            "AND r.blood_group = :bloodGroup " +
            "AND r.blood_component = :bloodComponent " +
            "GROUP BY r.facility_name, r.location " +
            "ORDER BY distance ASC " +
            "LIMIT 10", nativeQuery = true)
    List<FacilityLocationProjection> findTop10NearestBloodFacilityLocations(@Param("resourcesRequired") List<String> resourcesRequired,
                                                                            @Param("longitude") Double longitude,
                                                                            @Param("latitude") Double latitude,
                                                                            @Param("bloodGroup") String bloodGroup,
                                                                            @Param("bloodComponent") String bloodComponent);
    }
