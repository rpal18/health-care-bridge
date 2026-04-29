package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.AlreadyExistsException;
import com.Lifelink.HeathCareBridge.model.Patient;
import com.Lifelink.HeathCareBridge.model.Role;
import com.Lifelink.HeathCareBridge.payload.UserRequestDTO;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;
import com.Lifelink.HeathCareBridge.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO registerPatient(UserRequestDTO userRequestDTO){
        String phoneNumber = userRequestDTO.getPhoneNumber();
        String email = userRequestDTO.getEmail();
        Patient existingPatient = patientRepository.findPatientByPhoneNumberOrEmail(phoneNumber , email);
        if(existingPatient != null){
            throw new AlreadyExistsException("Patient with the same phone number or email already exists , kindly login instead!!");
        }
         Patient patient = new Patient();
         patient.setUserName(userRequestDTO.getName());
         patient.setEmail(email);
         patient.setPhoneNumber(phoneNumber);
         patient.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
         patient.setRoles(Set.of(Role.PATIENT));
         Patient savedPatient = patientRepository.save(patient);
         return modelMapper.map(savedPatient , UserResponseDTO.class);
    }
}
