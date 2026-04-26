package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.payload.FacilityDTO;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;

import java.util.List;
import java.util.UUID;

public interface FacilityService {
    FacilityResponseDTO addFacility(FacilityDTO facilityDTO);

    FacilityResponseDTO getFacilityById(UUID facilityId);

    String blockFacility(UUID facilityId);


    List<FacilityResponseDTO> getAllFacilities(Integer pageNumber , Integer pageSize);
}
