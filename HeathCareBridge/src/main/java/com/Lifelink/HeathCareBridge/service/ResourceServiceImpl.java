package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.exceptions.IllegalArgument;
import com.Lifelink.HeathCareBridge.model.*;
import com.Lifelink.HeathCareBridge.payload.BloodResourceDTO;
import com.Lifelink.HeathCareBridge.payload.BloodResourceResponseDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceResponseDTO;
import com.Lifelink.HeathCareBridge.repository.BloodRepository;
import com.Lifelink.HeathCareBridge.repository.FacilityRepository;
import com.Lifelink.HeathCareBridge.repository.ResourceRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResourceServiceImpl  implements ResourceService{

    private final ResourceRepository resourceRepository;

    private final ModelMapper modelMapper;
    private final FacilityRepository facilityRepository;

    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
    private final BloodRepository bloodRepository;

    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepository , ModelMapper
            modelMapper , FacilityRepository facilityRepository,
                               BloodRepository bloodRepository) {
        this.modelMapper = modelMapper;
        this.resourceRepository = resourceRepository;
        this.facilityRepository = facilityRepository;
        this.bloodRepository = bloodRepository;
    }
    @Override
    @Transactional
    public ResourceResponseDTO addResource(ResourceDTO resourceDTO, Admin admin) {
        Facility facility = admin.getFacility();
        if(facility == null){
            throw new DetailsNotFound("Admin is not associated with any facility");
        }
        String name = resourceDTO.getName();
        ResourceType resourceType = resourceDTO.getResourceType();
        int quantity = resourceDTO.getQuantity();

        Resource existingResource = resourceRepository.findByNameAndFacilityNameAndFacilityTypeAndResourceType(
                name, facility.getName(), facility.getType() , resourceType);

        if (existingResource != null) {
            if (quantity < 0) {
                throw new IllegalArgument("Quantity to add cannot be negative");
            }

            existingResource.setQuantity(existingResource.getQuantity() + quantity);
            existingResource.setAvailable(existingResource.getQuantity() > 0);
            existingResource.setLastUpdated(LocalDateTime.now());
            Resource updatedResource = resourceRepository.save(existingResource);

            return modelMapper.map(updatedResource, ResourceResponseDTO.class);
        }

        Resource resource = new Resource();
        resource.setName(name);
        resource.setFacilityType(facility.getType());
        resource.setQuantity(quantity);
        resource.setLastUpdated(LocalDateTime.now());
        resource.setAvailable(quantity > 0);
        resource.setResourceType(resourceType);
        resource.setFacilityName(facility.getName());
        resource.setFacilityRole(facility.getFacilityRole());

        Resource savedResource = resourceRepository.save(resource);

        return modelMapper.map(savedResource, ResourceResponseDTO.class);
    }




    @Override
    public List<Resource> getAllResource() {
        List<Resource> resources = resourceRepository.findAll();
        List<ResourceResponseDTO> response = resources.stream().
                map(element -> modelMapper.map(element , ResourceResponseDTO.class)).toList();
        return resources;
    }

    @Override
    @Transactional
    public BloodResourceResponseDTO addBloodResource(BloodResourceDTO bloodResourceDTO , Admin admin) {
        Facility facility = admin.getFacility();
        if(facility == null){
            throw new DetailsNotFound("Admin is not associated with any facility");
        }
        Blood existingResource = bloodRepository.
                findByNameAndFacilityNameAndFacilityTypeAndResourceTypeAndBloodComponent(
                bloodResourceDTO.getName(), facility.getName(), facility.getType() ,
                        ResourceType.BLOOD , bloodResourceDTO.getBloodComponent());
        int quantity = bloodResourceDTO.getQuantity();
        if (existingResource != null) {
            if (quantity < 0) {
                throw new IllegalArgument("Quantity to add cannot be negative");
            }

            existingResource.setQuantity(existingResource.getQuantity() + quantity);
            existingResource.setAvailable(existingResource.getQuantity() > 0);
            existingResource.setLastUpdated(LocalDateTime.now());
            Resource updatedResource = resourceRepository.save(existingResource);
            return modelMapper.map(updatedResource, BloodResourceResponseDTO.class);
        }
        Blood bloodResource = new Blood();
        bloodResource.setName(bloodResourceDTO.getName());
        bloodResource.setFacilityType(facility.getType());
        bloodResource.setQuantity(quantity);
        bloodResource.setLastUpdated(LocalDateTime.now());
        bloodResource.setAvailable(quantity > 0);
        bloodResource.setResourceType(ResourceType.BLOOD);
        bloodResource.setFacilityName(facility.getName());
        bloodResource.setFacilityRole(facility.getFacilityRole());
        bloodResource.setBloodComponent(bloodResourceDTO.getBloodComponent());
        bloodResource.setBloodGroup(bloodResourceDTO.getBloodGroup());
        Blood savedBloodResource = bloodRepository.save(bloodResource);
        return modelMapper.map(savedBloodResource, BloodResourceResponseDTO.class);
    }
}
