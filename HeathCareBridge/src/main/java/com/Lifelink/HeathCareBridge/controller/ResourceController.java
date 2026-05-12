package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.model.Admin;
import com.Lifelink.HeathCareBridge.model.Resource;
import com.Lifelink.HeathCareBridge.model.User;
import com.Lifelink.HeathCareBridge.payload.*;
import com.Lifelink.HeathCareBridge.repository.AdminRepository;
import com.Lifelink.HeathCareBridge.service.ResourceService;
import com.Lifelink.HeathCareBridge.util.AuthUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    public ResourceController(ResourceService resourceService, AuthUtil authUtil,
                              AdminRepository adminRepository) {
        this.resourceService = resourceService;
        this.authUtil = authUtil;
        this.adminRepository = adminRepository;
    }

    @PreAuthorize("hasAuthority('ORG_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ResourceResponseDTO> addResource(@RequestBody @Valid ResourceDTO resourceDTO) {
        User user = authUtil.loggedInUser();
        Admin admin = adminRepository.findById(user.getId()).orElseThrow(() ->
                new DetailsNotFound("Admin details not found for user: " + user.getUserName()));

        ResourceResponseDTO responseDTO = resourceService.addResource(resourceDTO, admin);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/add/blood-resource")
    public ResponseEntity<BloodResourceResponseDTO> addBloodResource(
            @RequestBody @Valid BloodResourceDTO bloodResourceDTO) {
        User user = authUtil.loggedInUser();
        Admin admin = adminRepository.findById(user.getId()).orElseThrow(() ->
                new DetailsNotFound("Admin details not found for user: " + user.getUserName()));

        BloodResourceResponseDTO responseDTO = resourceService.addBloodResource(bloodResourceDTO , admin);
        return ResponseEntity.ok(responseDTO);
    }
    @PreAuthorize("hasAuthority('ORG_ADMIN')")
    @PatchMapping("/update/{resourceId}/quantity/{quantity}")
    public ResponseEntity<ResourceResponseDTO> updateResourceQuantity(@PathVariable UUID resourceId,
                                                                      @PathVariable int quantity) {
        User user = authUtil.loggedInUser();
        Admin admin = adminRepository.findById(user.getId()).orElseThrow(() ->
                new DetailsNotFound("Admin details not found for user: " + user.getUserName()));
        ResourceResponseDTO responseDTO = resourceService.updateResourceQuantity(resourceId, quantity , admin);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasAuthority('ORG_ADMIN')")
    @PatchMapping("/allocate")
    public ResponseEntity<String> allocateResource(@RequestParam UUID resourceId ,
                                                                @RequestParam int quantity) {
        User user = authUtil.loggedInUser();
        Admin admin = adminRepository.findById(user.getId()).orElseThrow(() ->
                new DetailsNotFound("Admin details not found for user: " + user.getUserName()));
        int quantityLeft = resourceService.allocateResource(resourceId, admin , quantity);
        return new ResponseEntity<>(" this resource is left with " + quantityLeft , HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<List<ResourceResponseDTO>> getAllResources() {
        List<ResourceResponseDTO> ans = resourceService.getAllResource();
        return ResponseEntity.ok(ans);
    }

}
