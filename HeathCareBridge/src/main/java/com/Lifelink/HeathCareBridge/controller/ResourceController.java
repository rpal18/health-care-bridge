package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.model.Admin;
import com.Lifelink.HeathCareBridge.model.Resource;
import com.Lifelink.HeathCareBridge.model.User;
import com.Lifelink.HeathCareBridge.payload.BloodResourceDTO;
import com.Lifelink.HeathCareBridge.payload.BloodResourceResponseDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceResponseDTO;
import com.Lifelink.HeathCareBridge.repository.AdminRepository;
import com.Lifelink.HeathCareBridge.service.ResourceService;
import com.Lifelink.HeathCareBridge.util.AuthUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final AuthUtil authUtil;

    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
    private final AdminRepository adminRepository;

    public ResourceController(ResourceService resourceService , AuthUtil authUtil,
                              AdminRepository adminRepository) {
        this.resourceService = resourceService;
        this.authUtil = authUtil;
        this.adminRepository = adminRepository;
    }

    @PreAuthorize("hasAuthority('ORG_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ResourceResponseDTO> addResource( @RequestBody @Valid ResourceDTO resourceDTO) {
        User user = authUtil.loggedInUser();
        Admin admin = adminRepository.findById(user.getId()).orElseThrow(()->
                new DetailsNotFound("Admin details not found for user: " + user.getUserName()));

        ResourceResponseDTO responseDTO = resourceService.addResource(resourceDTO , admin);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/add/blood-resource")
    public ResponseEntity<BloodResourceResponseDTO> addBloodResource(@RequestBody @Valid BloodResourceDTO bloodResourceDTO) {
        BloodResourceResponseDTO responseDTO = resourceService.addBloodResource(bloodResourceDTO);
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping("/all")
    public ResponseEntity<List<Resource>> getAllResources(){
        List<Resource> ans = resourceService.getAllResource();
        return ResponseEntity.ok(ans);
    }

}
