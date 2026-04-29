package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.AlreadyExistsException;
import com.Lifelink.HeathCareBridge.model.Donor;
import com.Lifelink.HeathCareBridge.model.Role;
import com.Lifelink.HeathCareBridge.payload.DonorRequestDTO;
import com.Lifelink.HeathCareBridge.payload.DonorResponseDTO;
import com.Lifelink.HeathCareBridge.repository.DonorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class DonorServiceImpl implements DonorService {

    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public DonorResponseDTO registerDonor(DonorRequestDTO donorRequestDTO) {
        String email = donorRequestDTO.getEmail();
        String phoneNumber = donorRequestDTO.getPhoneNumber();
        Donor existingDonor = donorRepository.findDonorByEmailOrPhoneNumber(email, phoneNumber);
        if (existingDonor != null) {
            throw new AlreadyExistsException("Donor with the same email or phone number already exists " +
                    " , please login instead");
        }

        Donor donor = new Donor();
        donor.setUserName(donorRequestDTO.getName());
        donor.setEmail(email);
        donor.setPhoneNumber(phoneNumber);
        donor.setPassword(passwordEncoder.encode(donorRequestDTO.getPassword()));
        donor.setBloodGroup(donorRequestDTO.getBloodGroup());
        donor.setBloodComponent(donorRequestDTO.getBloodComponent());
        donor.setCity(donorRequestDTO.getCity());
        donor.setRoles(Set.of(Role.DONOR));
        donor.setAge(donorRequestDTO.getAge());
        Donor savedDonor = donorRepository.save(donor);
        return modelMapper.map(savedDonor, DonorResponseDTO.class);
    }

}
