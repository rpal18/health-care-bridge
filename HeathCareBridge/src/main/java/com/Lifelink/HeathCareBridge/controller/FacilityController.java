package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.AppConfig.AppConstant;
import com.Lifelink.HeathCareBridge.model.RequestedFacility;
import com.Lifelink.HeathCareBridge.model.User;
import com.Lifelink.HeathCareBridge.payload.FacilityDTO;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import com.Lifelink.HeathCareBridge.service.FacilityService;
import com.Lifelink.HeathCareBridge.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {
    @Autowired
    private FacilityService facilityService ;
    @Autowired
    private AuthUtil authUtil;

    //Logged In user who has made the request for facility creation will be the default facility admin but later
    //facility admin can be changed .
    @PostMapping("/request")
    public ResponseEntity<FacilityResponseDTO> requestFacility(
            @Valid @RequestBody FacilityDTO facilityDTO){
        User user = authUtil.loggedInUser();
        UUID userId = user.getId();
        FacilityResponseDTO response = facilityService.requestFacility(facilityDTO , userId);
        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    @PreAuthorize(("hasAuthority('SYSTEM_ADMIN')"))
    @PostMapping("/approve/{requestedFacilityId}")
    public ResponseEntity<FacilityResponseDTO> approveFacility(
            @PathVariable UUID requestedFacilityId){
        FacilityResponseDTO response = facilityService.approveFacility(requestedFacilityId);
        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    // getting facility
    @PreAuthorize(("hasAuthority('SYSTEM_ADMIN')"))
    @GetMapping("/{facilityId}")
    public ResponseEntity<FacilityResponseDTO> getFacility(@PathVariable UUID facilityId){
         FacilityResponseDTO response = facilityService.getFacilityById(facilityId);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    // rejecting facility creation request
    @PreAuthorize(("hasAuthority('SYSTEM_ADMIN')"))
    @PostMapping("/{requestedFacilityId}/reject")
    public ResponseEntity<String> rejectFacilityRequest(@PathVariable UUID requestedFacilityId){
        String message = facilityService.rejectFacilityRequest(requestedFacilityId);
        return new ResponseEntity<>(message , HttpStatus.OK);
    }


    // block facility
    @PreAuthorize(("hasAuthority('SYSTEM_ADMIN')"))
    @PatchMapping("/{facilityId}/block")
    public ResponseEntity<String> blockFacility(@PathVariable UUID facilityId){
        String message = facilityService.blockFacility(facilityId);
        return new ResponseEntity<>(message , HttpStatus.OK);
     }
     // getting all facilities in the database .
    @PreAuthorize(("hasAuthority('SYSTEM_ADMIN')"))
    @GetMapping("/all")
    public ResponseEntity<List<FacilityResponseDTO>> getAllFacilities(@RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
                                                                      @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE) Integer pageSize){
        List<FacilityResponseDTO> response = facilityService.getAllFacilities(pageNumber , pageSize);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PreAuthorize(("hasAuthority('SYSTEM_ADMIN')"))
    @DeleteMapping("/{facilityId}")
    public ResponseEntity<String> deleteFacility(@PathVariable UUID facilityId ,@RequestParam(required = false) String message){
        String response = facilityService.deleteFacility(facilityId , message);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }
    @PreAuthorize(("hasAuthority('SYSTEM_ADMIN')"))
    @PatchMapping("/{facilityId}")
    public ResponseEntity<String> restoreFacility(@PathVariable UUID facilityId ){
        String response = facilityService.restoreFacility(facilityId);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @PatchMapping("/{facilityId}/assign-admin/{adminId}")
    public ResponseEntity<String> assignFacilityAdmin(@PathVariable UUID facilityId, @PathVariable UUID adminId) {
        String message = facilityService.assignFacilityAdmin(facilityId, adminId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
