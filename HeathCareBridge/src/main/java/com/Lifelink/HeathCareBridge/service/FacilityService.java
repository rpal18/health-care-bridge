package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.model.RequestedFacility;
import com.Lifelink.HeathCareBridge.payload.FacilityDTO;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;

import java.util.List;
import java.util.UUID;

public interface FacilityService {
    FacilityResponseDTO approveFacility(UUID requestedFacilityId);

    FacilityResponseDTO getFacilityById(UUID facilityId);

    String blockFacility(UUID facilityId);


    List<FacilityResponseDTO> getAllFacilities(Integer pageNumber , Integer pageSize);

    String deleteFacility(UUID facilityId , String message);
    String restoreFacility(UUID facilityId);

    FacilityResponseDTO requestFacility(FacilityDTO facilityDTO , UUID id);

    String rejectFacilityRequest(UUID requestedFacilityId);

    String assignFacilityAdmin(UUID facilityId, UUID adminId);
}
