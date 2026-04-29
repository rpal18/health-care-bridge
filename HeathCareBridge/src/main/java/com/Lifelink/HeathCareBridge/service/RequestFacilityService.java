package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;

import java.util.List;

public interface RequestFacilityService {

    List<FacilityResponseDTO> getAllRequestedFacilities(Integer pageNumber, Integer pageSize );
}
