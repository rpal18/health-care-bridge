package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.AlreadyExistsException;
import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.model.*;
import com.Lifelink.HeathCareBridge.payload.FacilityDTO;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import com.Lifelink.HeathCareBridge.repository.AdminRepository;
import com.Lifelink.HeathCareBridge.repository.FacilityRepository;
import com.Lifelink.HeathCareBridge.repository.RequestedFacilityRepository;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FacilityServiceImpl implements FacilityService{

    @Autowired
    private  FacilityRepository facilityRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RequestedFacilityRepository requestedFacilityRepository;

    @Autowired
    private AdminRepository adminRepository;

    private final Logger logger = LoggerFactory.getLogger(FacilityServiceImpl.class);

    @Override
    @Transactional
    public FacilityResponseDTO approveFacility(UUID requestedFacilityId) {
        RequestedFacility requestedFacility = requestedFacilityRepository.findById(requestedFacilityId)
                .orElseThrow(() -> new DetailsNotFound("Requested Facility not found with id: " + requestedFacilityId));
        String phoneNumber = requestedFacility.getPhoneNumber();
        String email = requestedFacility.getEmail();
        Facility existingFacility = facilityRepository.findFacilityByPhoneNumberOrEmail(phoneNumber , email);
        if(existingFacility != null){
                throw new AlreadyExistsException("Facility with the same phone number or email already exists");
        }

        Facility facility = getFacilityFromRequestedFacility(requestedFacility);
        facility.setApprovedOn(LocalDateTime.now());
        Facility savedFacility = facilityRepository.save(facility);
        requestedFacilityRepository.delete(requestedFacility);
        return modelMapper.map(savedFacility , FacilityResponseDTO.class);
    }

    @Override
    public FacilityResponseDTO getFacilityById(UUID facilityId) {
        Facility facility = facilityRepository.findById(facilityId).orElseThrow(() ->
                new DetailsNotFound("Facility not found with id: " + facilityId));
        return modelMapper.map(facility , FacilityResponseDTO.class);
    }

    @Override
    public String blockFacility(UUID facilityId) {

        Facility facility = facilityRepository.findById(facilityId).orElseThrow(() ->
                new DetailsNotFound("Facility not found with id: " + facilityId));
        facility.setFacilityStatus(FacilityStatus.BLOCKED);
        facilityRepository.save(facility);
        return "Facility with id: " + facilityId + " has been blocked successfully.";
    }

    @Override
    public List<FacilityResponseDTO> getAllFacilities(Integer pageNumber , Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber , pageSize);
        Page<Facility> facilitiesPage = facilityRepository.findAll(pageable);
        List<Facility> facilities = facilitiesPage.getContent();
        if(facilities.isEmpty()){
            throw new DetailsNotFound("No facilities found in the database.");
        }
        return facilities.stream().map(facility -> modelMapper.map(facility , FacilityResponseDTO.class)).toList();
    }


    @Override
    @Transactional
    public String deleteFacility(UUID facilityId , String deletionReason) {
        Facility facility = facilityRepository.findByIdAndIsDeletedFalse(facilityId)
                .orElseThrow(() -> new DetailsNotFound("Facility not found with id: " + facilityId));

        facility.setDeleted(true);
        facility.setDeletedAt(LocalDateTime.now());
        facility.setDeletionReason(deletionReason);
        facilityRepository.save(facility);

        return "Facility with id: " + facilityId + " has been soft deleted successfully.";
    }

    @Override
    @Transactional
    public String restoreFacility(UUID facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new DetailsNotFound("Facility not found with id: " + facilityId));

        if (!facility.isDeleted()) {
            return "Facility is already active.";
        }

        facility.setDeleted(false);
        facility.setDeletedAt(null);
        facility.setDeletionReason("restored by admin");
        facilityRepository.save(facility);

        return "Facility with id: " + facilityId + " has been restored successfully.";
    }

    @Override
    @Transactional
    public FacilityResponseDTO requestFacility(FacilityDTO facilityDTO , UUID userId) {
        String phoneNumber = facilityDTO.getPhoneNumber();
        String email = facilityDTO.getEmail();
        Facility existingFacility = facilityRepository.findFacilityByPhoneNumberOrEmail(phoneNumber , email);
        if(existingFacility != null){
            throw new AlreadyExistsException("Facility with the same phone number or email already exists");
        }
        RequestedFacility requestedFacility = requestedFacilityRepository.findByPhoneNumberOrEmail(phoneNumber , email);
        if(requestedFacility != null){
            throw new AlreadyExistsException("A facility request with the same phone number or email already exists");
        }
        requestedFacility = getFacility(facilityDTO);
        requestedFacility.setFacilityRequesterId(userId);
        RequestedFacility savedRequest = requestedFacilityRepository.save(requestedFacility);
        return modelMapper.map(savedRequest , FacilityResponseDTO.class);
    }

    @Override
    public String rejectFacilityRequest(UUID requestedFacilityId) {
        RequestedFacility requestedFacility = requestedFacilityRepository.findById(requestedFacilityId)
                .orElseThrow(() -> new DetailsNotFound("Requested Facility not found with id: " + requestedFacilityId));

        requestedFacility.setFacilityStatus(FacilityStatus.REJECTED);
        return "Facility request with id: " + requestedFacilityId ;
    }

    @Override
    @Transactional
    public String assignFacilityAdmin(UUID facilityId, UUID adminId) {

        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new DetailsNotFound("Facility not found"));

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new DetailsNotFound("Admin not found with id: " + adminId));
        facility.setFacilityAdmin(admin);
        admin.setFacility(facility);
        return "Admin with id: " + adminId + " has been assigned to facility with id: " + facilityId + " successfully.";
    }

    private static Facility getFacilityFromRequestedFacility(RequestedFacility requestedFacility) {
        if(requestedFacility.getFacilityStatus() != FacilityStatus.PENDING){
            throw new IllegalArgumentException("Only pending facility requests can be approved.");
        }
        Facility facility = new Facility();
        facility.setName(requestedFacility.getName());
        facility.setAddress(requestedFacility.getAddress());
        facility.setType(requestedFacility.getType());
        facility.setFacilityRole(requestedFacility.getFacilityRole());
        facility.setDirectPatientCare(requestedFacility.isDirectPatientCare());
        facility.setIs24x7(requestedFacility.getIs24x7());
        facility.setPhoneNumber(requestedFacility.getPhoneNumber());
        facility.setEmail(requestedFacility.getEmail());
        facility.setLatitude(requestedFacility.getLatitude());
        facility.setLongitude(requestedFacility.getLongitude());
        facility.setFacilityStatus(FacilityStatus.ACTIVE);
        return facility;
    }

    private static RequestedFacility getFacility(FacilityDTO facilityDTO) {
        RequestedFacility requestedFacility = new RequestedFacility();
        requestedFacility.setName(facilityDTO.getName());
        requestedFacility.setAddress(facilityDTO.getAddress());
        requestedFacility.setType(facilityDTO.getType());
        requestedFacility.setFacilityRole(facilityDTO.getFacilityRole());
        requestedFacility.setDirectPatientCare(facilityDTO.isDirectPatientCare());
        requestedFacility.setIs24x7(facilityDTO.getIs24x7());
        requestedFacility.setPhoneNumber(facilityDTO.getPhoneNumber());
        requestedFacility.setEmail(facilityDTO.getEmail());
        requestedFacility.setLatitude(facilityDTO.getLatitude());
        requestedFacility.setLongitude(facilityDTO.getLongitude());
        requestedFacility.setFacilityStatus(FacilityStatus.PENDING);
        return requestedFacility;
    }
}
