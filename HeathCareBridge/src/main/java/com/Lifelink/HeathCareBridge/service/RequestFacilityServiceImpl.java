package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.model.RequestedFacility;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import com.Lifelink.HeathCareBridge.repository.RequestedFacilityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestFacilityServiceImpl implements RequestFacilityService{
    @Autowired
    private RequestedFacilityRepository requestedFacilitiesRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<FacilityResponseDTO> getAllRequestedFacilities(Integer pageNumber, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(pageNumber , pageSize);
        Page<RequestedFacility> requestedFacilities = requestedFacilitiesRepository.findAll(pageRequest);
        List<RequestedFacility> requestedFacilitiesList = requestedFacilities.getContent();
        if(requestedFacilitiesList.isEmpty()){
            throw new RuntimeException("No Facility Creation Request Found");
        }
        List<FacilityResponseDTO> responseDTOS = requestedFacilitiesList.stream()
                .map(requestedFacility -> modelMapper.map(requestedFacility , FacilityResponseDTO.class)).toList();
        return responseDTOS;
    }
}
