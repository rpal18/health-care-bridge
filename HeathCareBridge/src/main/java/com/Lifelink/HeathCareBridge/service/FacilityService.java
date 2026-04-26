package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.payload.FacilityDTO;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;

public interface FacilityService {
    FacilityResponseDTO addFacility(FacilityDTO facilityDTO);
}
