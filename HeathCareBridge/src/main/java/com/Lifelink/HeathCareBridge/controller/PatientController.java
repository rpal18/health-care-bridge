package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.payload.UserRequestDTO;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;
import com.Lifelink.HeathCareBridge.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
     public PatientController(PatientService patientService){
        this.patientService = patientService;
     }

     @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerPatient(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO response = patientService.registerPatient(userRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
