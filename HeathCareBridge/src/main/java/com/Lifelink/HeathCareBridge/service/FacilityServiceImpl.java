package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.AlreadyExistsException;
import com.Lifelink.HeathCareBridge.model.Facility;
import com.Lifelink.HeathCareBridge.payload.FacilityDTO;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import com.Lifelink.HeathCareBridge.repository.FacilityRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return facility;
    }
}
