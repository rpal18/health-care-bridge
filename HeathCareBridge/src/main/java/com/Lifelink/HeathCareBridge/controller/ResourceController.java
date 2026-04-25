package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.model.Resource;
import com.Lifelink.HeathCareBridge.payload.BloodResourceDTO;
import com.Lifelink.HeathCareBridge.payload.BloodResourceResponseDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceDTO;
import com.Lifelink.HeathCareBridge.payload.ResourceResponseDTO;
import com.Lifelink.HeathCareBridge.service.ResourceService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResourceResponseDTO> addResource( @RequestBody @Valid ResourceDTO resourceDTO) {
        logger.debug("Received request to add resource: {} to facility: {} of type: {} with quantity: {}" , resourceDTO.getName() ,
                resourceDTO.getFacilityName() , resourceDTO.getFacilityType() , resourceDTO.getQuantity());

        ResourceResponseDTO responseDTO = resourceService.addResource(resourceDTO);
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
