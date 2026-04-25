package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.IllegalArgument;
import com.Lifelink.HeathCareBridge.model.FacilityType;
import com.Lifelink.HeathCareBridge.model.Resource;
import com.Lifelink.HeathCareBridge.model.ResourceType;
import com.Lifelink.HeathCareBridge.payload.BloodResourceDTO;
import com.Lifelink.HeathCareBridge.payload.BloodResourceResponseDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceResponseDTO;
import com.Lifelink.HeathCareBridge.repository.ResourceRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResourceServiceImpl  implements ResourceService{

    private final ResourceRepository resourceRepository;

    private final ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepository , ModelMapper modelMapper) {
         this.modelMapper = modelMapper;
        this.resourceRepository = resourceRepository;
    }
    @Override
    public ResourceResponseDTO addResource(ResourceDTO resourceDTO) {
         logger.debug("Adding resource: {} to facility: {} of type: {} with quantity: {}" , resourceDTO.getName() , resourceDTO.getFacilityName() , resourceDTO.getFacilityType() , resourceDTO.getQuantity());
         FacilityType type = resourceDTO.getFacilityType();
         logger.debug(" this is facility type : " + type);
         String name = resourceDTO.getName();
         logger.debug(" this is resource name : " + name);
         ResourceType resourceType = resourceDTO.getResourceType();
         logger.debug( " this is resource type : " + resourceType);
         String facilityName = resourceDTO.getFacilityName();
         logger.debug( " this is facility name : " + facilityName);
         int quantity = resourceDTO.getQuantity();
         Resource existingResource = resourceRepository.findByNameAndFacilityNameAndFacilityTypeAndResourceType(name ,
                 facilityName , type , resourceType);

         if(existingResource != null) {

             logger.info("Resource already exists. Updating quantity for resource: {} in facility: {} of type: {} with quantity: {}" , name , facilityName , type , quantity);
             if(quantity < 0){
                    throw new IllegalArgument("Quantity to add cannot be negative");
             }
             existingResource.setQuantity(existingResource.getQuantity() + quantity);
             existingResource.setAvailable(existingResource.getQuantity() > 0);
             existingResource.setLastUpdated(LocalDateTime.now());
             Resource updatedResource = resourceRepository.save(existingResource);
             return modelMapper.map(updatedResource , ResourceResponseDTO.class);
         }
        Resource resource = new Resource();
        resource.setName(resourceDTO.getName());
        resource.setFacilityType(resourceDTO.getFacilityType());
        resource.setQuantity(resourceDTO.getQuantity());
        resource.setLastUpdated(LocalDateTime.now());
        resource.setAvailable(resourceDTO.getQuantity() > 0);
        resource.setResourceType(resourceDTO.getResourceType());
        resource.setFacilityName(resourceDTO.getFacilityName());

        Resource savedResource = resourceRepository.save(resource);
        System.out.println(savedResource);
        logger.info("Resource added successfully: {} to facility: {} of type: {} with quantity: {}" ,
                name , facilityName , type , quantity);
        return modelMapper.map(savedResource , ResourceResponseDTO.class);


    }

    @Override
    public List<Resource> getAllResource() {
        List<Resource> resources = resourceRepository.findAll();
        List<ResourceResponseDTO> response = resources.stream().
                map(element -> modelMapper.map(element , ResourceResponseDTO.class)).toList();
        return resources;
    }

    @Override
    public BloodResourceResponseDTO addBloodResource(BloodResourceDTO bloodResourceDTO) {
        return null;
    }
}
