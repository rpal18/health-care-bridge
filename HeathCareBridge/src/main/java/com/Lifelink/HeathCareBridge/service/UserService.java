package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.payload.UserRequestDTO;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;

public interface UserService {

    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);
}
