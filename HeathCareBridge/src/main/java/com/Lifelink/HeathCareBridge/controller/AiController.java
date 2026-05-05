package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.model.ResourceType;
import com.Lifelink.HeathCareBridge.payload.AiResponse;
import com.Lifelink.HeathCareBridge.payload.LocationDTO;
import com.Lifelink.HeathCareBridge.payload.NearByResponseDTO;
import com.Lifelink.HeathCareBridge.projection.FacilityLocationProjection;
import com.Lifelink.HeathCareBridge.repository.ResourceRepository;
import com.Lifelink.HeathCareBridge.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency")
public class AiController {

    private final AiService aiTriageService;
    private final ResourceRepository resourceRepository;

    public AiController(AiService aiTriageService, ResourceRepository resourceRepository) {
        this.aiTriageService = aiTriageService;
        this.resourceRepository = resourceRepository;
    }

    @PostMapping("/find-help")
    public ResponseEntity<?> findNearestHelp(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {

        try {

            AiResponse triageData = aiTriageService.analyzeEmergency(description, image);

            List<FacilityLocationProjection> nearestFacilities;

            if (triageData.requiredResources().contains(ResourceType.BLOOD)
                    && triageData.bloodGroup() != null
                    && triageData.bloodComponent() != null) {
                List<String> requiredResources = triageData.requiredResources().stream().
                        map(Enum::name).toList();

                nearestFacilities = resourceRepository.findTop10NearestBloodFacilityLocations(
                        requiredResources,
                        longitude,
                        latitude,
                        triageData.bloodGroup().name(),
                        triageData.bloodComponent().name()
                );
            } else {
                List<String> requiredResources = triageData.requiredResources().stream().
                        map(Enum::name).toList();
                nearestFacilities = resourceRepository.findTop10NearestFacilityLocations(
                        requiredResources,
                        longitude,
                        latitude
                );
            }
            List<NearByResponseDTO> response = nearestFacilities.stream().map(facilityProjection ->{
                Double lon = facilityProjection.getLongitude();

                Double lat = facilityProjection.getLatitude();
                LocationDTO locationDTO = new LocationDTO(lat, lon);
                return new NearByResponseDTO(facilityProjection.getFacilityName(), locationDTO, facilityProjection.getDistance());
            }).toList();
            return ResponseEntity.ok(new TriageResultPayload(triageData, response));

        } catch (Exception e) {
            Map<String , Object> map = new HashMap<>();
            map.put("error", "Failed to process the request: " + e.getMessage());
            map.put("status" , "error");
            map.put("timestamp" , System.currentTimeMillis());
            map.put("status code" , 500);
            return ResponseEntity.status(500).body(map);
        }
    }
    public record TriageResultPayload(AiResponse aiAnalysis, List<NearByResponseDTO> recommendedFacilities) {}
}