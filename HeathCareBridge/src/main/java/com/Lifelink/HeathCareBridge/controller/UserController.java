package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.AppConfig.AppConstant;
import com.Lifelink.HeathCareBridge.payload.UserResponseDTO;
import com.Lifelink.HeathCareBridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    @GetMapping("all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@RequestParam(required = false , defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
                                                             @RequestParam(required = false , defaultValue = AppConstant.PAGE_SIZE) Integer pageSize){
        List<UserResponseDTO> users = userService.getAllUsers(pageNumber, pageSize);
        return ResponseEntity.ok(users);
    }
}
