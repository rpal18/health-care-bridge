package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.payload.UserRequestDTO;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;
import com.Lifelink.HeathCareBridge.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/public/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    //register as admin
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerAdmin(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO = adminService.registerAdmin(userRequestDTO);
        return new ResponseEntity<>(userResponseDTO , HttpStatus.CREATED);
    }
    //login as admin
//    @PostMapping("/login")
//    public ResponseEntity<String> loginAdmin(@Valid @RequestBody UserRequestDTO userRequestDTO){
//        String token = adminService.loginAdmin(userRequestDTO);
//        return new ResponseEntity<>(token , HttpStatus.OK);
//    }
}
