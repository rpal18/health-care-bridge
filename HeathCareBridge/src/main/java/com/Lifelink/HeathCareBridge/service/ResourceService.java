package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.model.Admin;
import com.Lifelink.HeathCareBridge.model.Facility;
import com.Lifelink.HeathCareBridge.model.Resource;
import com.Lifelink.HeathCareBridge.payload.*;

import java.util.List;
import java.util.UUID;

public interface ResourceService {

    ResourceResponseDTO addResource(ResourceDTO resourceDTO , Admin admin);

    List<Resource> getAllResource();

    BloodResourceResponseDTO addBloodResource(BloodResourceDTO bloodResourceDTO , Admin admin);

    ResourceResponseDTO updateResourceQuantity(UUID resourceId, int quantity , Admin admin);
    int allocateResource(UUID resourceID , Admin adminm, int quantity);
    List<NearByResponseDTO> getFacilitiesWhereResourceIsAvailable(ResourceRequestDTO resourceRequestDTO);


}

