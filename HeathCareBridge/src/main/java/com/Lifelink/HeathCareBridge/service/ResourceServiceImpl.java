package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.exceptions.IllegalArgument;
import com.Lifelink.HeathCareBridge.model.*;
import com.Lifelink.HeathCareBridge.payload.*;
import com.Lifelink.HeathCareBridge.projection.FacilityLocationProjection;
import com.Lifelink.HeathCareBridge.repository.BloodRepository;
import com.Lifelink.HeathCareBridge.repository.FacilityRepository;
import com.Lifelink.HeathCareBridge.repository.ResourceRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final ModelMapper modelMapper;
    private final FacilityRepository facilityRepository;
    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
    private final BloodRepository bloodRepository;

    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepository, ModelMapper
            modelMapper, FacilityRepository facilityRepository,
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
        if (facility == null) {
            throw new DetailsNotFound("Admin is not associated with any facility");
        }
        String name = resourceDTO.getName();
        ResourceType resourceType = resourceDTO.getResourceType();
        int quantity = resourceDTO.getQuantity();

        Resource existingResource = resourceRepository.findByNameAndFacilityNameAndFacilityTypeAndResourceType(
                name, facility.getName(), facility.getType(), resourceType);
        if (quantity < 0) {
            throw new IllegalArgument("Quantity to add cannot be negative");
        }
        if (existingResource != null) {
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
        resource.setFacilityEmail(facility.getEmail());
        resource.setFacilityPhoneNumber(facility.getPhoneNumber());
        resource.setLocation(facility.getLocation());
        Resource savedResource = resourceRepository.save(resource);
        return modelMapper.map(savedResource, ResourceResponseDTO.class);
    }

    @Override
    public List<ResourceResponseDTO> getAllResource() {
        List<Resource> resources = resourceRepository.findAll();
        List<ResourceResponseDTO> response = resources.stream().
                map(element -> {
                    ResourceResponseDTO dto = modelMapper.map(element, ResourceResponseDTO.class);
                    if (element.getLocation() != null) {
                        dto.setLatitude(element.getLocation().getY());
                        dto.setLongitude(element.getLocation().getX());
                    }
                    return dto;
                }).toList();
        return response;
    }

    @Override
    @Transactional
    public BloodResourceResponseDTO addBloodResource(BloodResourceDTO bloodResourceDTO, Admin admin) {
        Facility facility = admin.getFacility();
        if (facility == null) {
            throw new DetailsNotFound("Admin is not associated with any facility");
        }
        Blood existingResource = bloodRepository.
                findByNameAndFacilityNameAndFacilityTypeAndResourceTypeAndBloodComponent(
                        bloodResourceDTO.getName(), facility.getName(), facility.getType(),
                        ResourceType.BLOOD, bloodResourceDTO.getBloodComponent());
        int quantity = bloodResourceDTO.getQuantity();
        if (quantity < 0) {
            throw new IllegalArgument("Quantity to add cannot be negative");
        }
        if (existingResource != null) {
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
        bloodResource.setFacilityEmail(facility.getEmail());
        bloodResource.setFacilityPhoneNumber(facility.getPhoneNumber());
        bloodResource.setLocation(facility.getLocation());
        Blood savedBloodResource = bloodRepository.save(bloodResource);
        return modelMapper.map(savedBloodResource, BloodResourceResponseDTO.class);
    }

    @Override
    @Transactional
    public ResourceResponseDTO updateResourceQuantity(UUID resourceId, int quantity, Admin admin) {
        Resource resource = resourceRepository.findById(resourceId).orElseThrow(() -> new DetailsNotFound("Resource not " +
                "found with id: " + resourceId));
        Facility facility = admin.getFacility();
        if (facility == null) {
            throw new DetailsNotFound("Admin is not associated with any facility");
        }
        if (quantity < 0) {
            throw new IllegalArgument("Quantity to add cannot be negative");
        }
        resource.setQuantity(resource.getQuantity() + quantity);
        resource.setAvailable(resource.getQuantity() > 0);
        resource.setLastUpdated(LocalDateTime.now());
        Resource updatedResource = resourceRepository.save(resource);
        return modelMapper.map(updatedResource, ResourceResponseDTO.class);
    }

    @Override
    @Transactional
    public int allocateResource(UUID resourceId, Admin admin, int quantity) {
        Resource resource = resourceRepository.findById(resourceId).orElseThrow(() -> new DetailsNotFound("Resource not " +
                "found with id: " + resourceId));
        UUID facilityID = admin.getFacility().getId();
        if (facilityID == null) {
            throw new DetailsNotFound("Admin is not associated with any facility");
        }
        String facilityEmail = resource.getFacilityEmail();
        String facilityPhoneNumber = resource.getFacilityPhoneNumber();
        Facility facility = facilityRepository.findFacilityByEmailAndPhoneNumber(facilityEmail, facilityPhoneNumber).
                orElseThrow(() -> new DetailsNotFound("No facility found!!"));
        if (facilityID != facility.getId()) {
            throw new AccessDeniedException("Not authorized!!");
        }

        if (quantity < 0) {
            throw new IllegalArgument("Quantity to allocate cannot be negative");
        }
        if (resource.getQuantity() < quantity) {
            throw new IllegalArgument("Not enough quantity available to allocate");
        }
        resource.setQuantity(resource.getQuantity() - quantity);
        resource.setAvailable(resource.getQuantity() > 0);
        resource.setLastUpdated(LocalDateTime.now());
        Resource res = resourceRepository.save(resource);
        return res.getQuantity();

    }

    @Override
    public List<NearByResponseDTO> getFacilitiesWhereResourceIsAvailable(ResourceRequestDTO resourceRequestDTO) {
        Double latitude = resourceRequestDTO.getLatitude();
        Double longitude = resourceRequestDTO.getLongitude();

        if (resourceRequestDTO.getResourceTypes() == null || resourceRequestDTO.getResourceTypes().isEmpty()) {
            throw new IllegalArgument("Resource types cannot be null or empty");
        }

        List<FacilityLocationProjection> facilitiesProjection;
        if (resourceRequestDTO.getResourceTypes().contains(ResourceType.BLOOD)) {
            List<String> resourceTypeNames = resourceRequestDTO.getResourceTypes().stream()
                    .map(Enum::name)
                    .toList();
            String bloodComponent = resourceRequestDTO.getBloodComponent().name();
            String bloodGroup = resourceRequestDTO.getBloodGroup().name();
            facilitiesProjection  = resourceRepository.findTop10NearestBloodFacilityLocations(
                    resourceTypeNames, longitude, latitude, bloodGroup, bloodComponent);
        } else {
            List<String> resourceTypeNames = resourceRequestDTO.getResourceTypes().stream()
                    .map(Enum::name)
                    .toList();
            facilitiesProjection = resourceRepository.findTop10NearestFacilityLocations(
                    resourceTypeNames,longitude ,latitude);
        }

        List<NearByResponseDTO> responseList = facilitiesProjection.stream().map(proj -> {
            Double lat = proj.getLatitude() !=null ? proj.getLatitude(): null;
            Double lon = proj.getLongitude() !=null ? proj.getLongitude(): null;
            LocationDTO locationObj = new LocationDTO(lat, lon);
            return new NearByResponseDTO(proj.getFacilityName(), locationObj, proj.getDistance()*0.001);
        }).toList();
        return responseList;
    }

}

