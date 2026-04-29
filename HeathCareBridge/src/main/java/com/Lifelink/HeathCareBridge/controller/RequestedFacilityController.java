package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.AppConfig.AppConstant;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import com.Lifelink.HeathCareBridge.service.RequestFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/requestedFacility")
public class RequestedFacilityController {

    @Autowired
    private RequestFacilityService requestedFacilityService;


    @GetMapping("/all")
    public ResponseEntity<List<FacilityResponseDTO>> getAllRequestedFacilities(
            @RequestParam(required = false , defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber ,
            @RequestParam(required = false , defaultValue = AppConstant.PAGE_SIZE) Integer pageSize){
        List<FacilityResponseDTO> response = requestedFacilityService.
                getAllRequestedFacilities(pageNumber , pageSize);
        return ResponseEntity.ok(response);
    }
}
