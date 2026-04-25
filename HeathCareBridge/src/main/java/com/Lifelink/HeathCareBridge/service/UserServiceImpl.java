package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.payload.UserRequestDTO;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Override
    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        return null;
    }
}
