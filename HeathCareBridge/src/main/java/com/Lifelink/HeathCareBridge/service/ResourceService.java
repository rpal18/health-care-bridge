package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.model.Admin;
import com.Lifelink.HeathCareBridge.model.Resource;
import com.Lifelink.HeathCareBridge.payload.BloodResourceDTO;
import com.Lifelink.HeathCareBridge.payload.BloodResourceResponseDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceResponseDTO;

import java.util.List;

public interface ResourceService {

    ResourceResponseDTO addResource(ResourceDTO resourceDTO , Admin admin);

    List<Resource> getAllResource();

    BloodResourceResponseDTO addBloodResource(BloodResourceDTO bloodResourceDTO);
}

