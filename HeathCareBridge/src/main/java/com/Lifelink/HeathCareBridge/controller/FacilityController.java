package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.payload.FacilityDTO;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import com.Lifelink.HeathCareBridge.service.FacilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {
    @Autowired
    private FacilityService facilityService ;

    // Note : these endpoints are only accessible to admin users as they are responsible for managing the facilities
    // and resources in the healthcare system.
    // 1) get facility by id .
    // 2) add new facility
    // 3) add facility where a particular resource exists
 // so this endpoint is going to be public and it does not require any kind of login ..   // 4) get all facilities where a particular resource exists ( users can search for facilities based on the resources they need, such as ICU beds, ventilators, or specialized medical equipment. This endpoint would return a list of facilities that have the specified resource available. )
    // 5) get all facilities in the dataBase .

    // Add new facility .
    @PreAuthorize(("hasAuthority('SYSTEM_ADMIN')"))
    @PostMapping("/add")
    public ResponseEntity<FacilityResponseDTO> addFacility(
            @Valid @RequestBody FacilityDTO facilityDTO){
        FacilityResponseDTO response = facilityService.addFacility(facilityDTO);
        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }
}
