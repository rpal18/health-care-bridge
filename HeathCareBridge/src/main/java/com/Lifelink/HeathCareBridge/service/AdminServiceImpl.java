package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.model.Admin;
import com.Lifelink.HeathCareBridge.model.Role;
import com.Lifelink.HeathCareBridge.model.User;
import com.Lifelink.HeathCareBridge.payload.UserRequestDTO;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;
import com.Lifelink.HeathCareBridge.repository.AdminRepository;
import com.Lifelink.HeathCareBridge.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDTO registerAdmin(UserRequestDTO userRequestDTO) {
        String phoneNumber = userRequestDTO.getPhoneNumber();
        String email = userRequestDTO.getEmail();
        Admin existingAdmin = adminRepository.findAdminByPhoneNumberOrEmail(phoneNumber , email);
        if(existingAdmin != null){
            throw new DetailsNotFound("Admin with the same phone number or email already exists");
        }
         Admin admin = new Admin();
         admin.setUserName(userRequestDTO.getName());
         admin.setEmail(email);
         admin.setPhoneNumber(phoneNumber);
         admin.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
         admin.setRoles(Set.of(Role.ORG_ADMIN));
         Admin savedAdmin = adminRepository.save(admin);
         return modelMapper.map(savedAdmin , UserResponseDTO.class);
    }


}
