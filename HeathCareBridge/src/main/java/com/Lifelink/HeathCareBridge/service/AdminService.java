package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.payload.UserRequestDTO;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;

public interface AdminService {

    UserResponseDTO registerAdmin(UserRequestDTO adminDTO);

//     UserResponseDTO updateAdmin(UserResponseDTO adminDTO, String adminId);
//
//     UserResponseDTO deleteAdmin(String adminId);
}
