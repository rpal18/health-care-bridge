package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.model.User;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import com.Lifelink.HeathCareBridge.payload.UserRequestDTO;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;
import com.Lifelink.HeathCareBridge.service.AdminService;
import com.Lifelink.HeathCareBridge.service.FacilityService;
import com.Lifelink.HeathCareBridge.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/public/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private FacilityService facilityService;

    //register as admin
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerAdmin(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO = adminService.registerAdmin(userRequestDTO);
        return new ResponseEntity<>(userResponseDTO , HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ORG_ADMIN')")
    @GetMapping("/my-facility")
    public ResponseEntity<FacilityResponseDTO> getFacilityDetails(){
        User user = authUtil.loggedInUser();
        FacilityResponseDTO response = facilityService.getFacilityDetailsForOrgAdmin(user);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

}
