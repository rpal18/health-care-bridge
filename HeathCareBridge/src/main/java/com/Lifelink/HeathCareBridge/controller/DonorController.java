package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.payload.DonorRequestDTO;
import com.Lifelink.HeathCareBridge.payload.DonorResponseDTO;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;
import com.Lifelink.HeathCareBridge.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/public/donor")
public class DonorController {
    @Autowired
    private DonorService donorService;
    @PostMapping("/register")
    public ResponseEntity<DonorResponseDTO> registerForDonor(@RequestBody @Valid DonorRequestDTO donorRequestDTO){
        DonorResponseDTO donorResponseDTO = donorService.registerDonor(donorRequestDTO);
        return new ResponseEntity<>(donorResponseDTO , HttpStatus.CREATED);
    }
}
