package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.AlreadyExistsException;
import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.model.Facility;
import com.Lifelink.HeathCareBridge.model.FacilityStatus;
import com.Lifelink.HeathCareBridge.payload.FacilityDTO;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import com.Lifelink.HeathCareBridge.repository.FacilityRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FacilityServiceImpl implements FacilityService{

    @Autowired
    private  FacilityRepository facilityRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    @Transactional
    public FacilityResponseDTO addFacility(FacilityDTO facilityDTO) {
        String phoneNumber = facilityDTO.getPhoneNumber();
        String email = facilityDTO.getEmail();
        Facility existingFacility = facilityRepository.findFacilityByPhoneNumberOrEmail(phoneNumber , email);
        if(existingFacility != null){
                throw new AlreadyExistsException("Facility with the same phone number or email already exists");
        }

        Facility facility = getFacility(facilityDTO);
        Facility savedFacility = facilityRepository.save(facility);

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


    private static Facility getFacility(FacilityDTO facilityDTO) {
        Facility facility = new Facility();
        facility.setName(facilityDTO.getName());
        facility.setAddress(facilityDTO.getAddress());
        facility.setType(facilityDTO.getType());
        facility.setFacilityRole(facilityDTO.getFacilityRole());
        facility.setDirectPatientCare(facilityDTO.isDirectPatientCare());
        facility.setIs24x7(facilityDTO.getIs24x7());
        facility.setPhoneNumber(facilityDTO.getPhoneNumber());
        facility.setEmail(facilityDTO.getEmail());
        facility.setLatitude(facilityDTO.getLatitude());
        facility.setLongitude(facilityDTO.getLongitude());
        facility.setFacilityRole(facilityDTO.getFacilityRole());
        facility.setFacilityStatus(FacilityStatus.ACTIVE);
        return facility;
    }
}
