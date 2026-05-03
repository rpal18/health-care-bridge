package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.AppConfig.AppConstant;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceRequestDTO;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;
import com.Lifelink.HeathCareBridge.service.ResourceService;
import com.Lifelink.HeathCareBridge.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @GetMapping("all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@RequestParam(required = false , defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
                                                             @RequestParam(required = false , defaultValue = AppConstant.PAGE_SIZE) Integer pageSize){
        List<UserResponseDTO> users = userService.getAllUsers(pageNumber, pageSize);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/nearby-help")
    public ResponseEntity<List<FacilityResponseDTO>> getNearByHelp(@RequestBody @Valid ResourceRequestDTO resourceRequestDTO){
        List<FacilityResponseDTO> response = resourceService.getFacilitiesWhereResourceIsAvailable(resourceRequestDTO);
        return ResponseEntity.ok(response);
    }
}
