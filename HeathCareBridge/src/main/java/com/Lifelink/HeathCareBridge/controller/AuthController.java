package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.model.Admin;
import com.Lifelink.HeathCareBridge.model.Role;
import com.Lifelink.HeathCareBridge.payload.LoginRequest;
import com.Lifelink.HeathCareBridge.payload.UserInfoResponse;
import com.Lifelink.HeathCareBridge.repository.AdminRepository;
import com.Lifelink.HeathCareBridge.security.JwtUtils;
import com.Lifelink.HeathCareBridge.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;



    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          AdminRepository adminRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;

        this.adminRepository = adminRepository;
    }

   private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AdminRepository adminRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        try {
            Authentication unauthenticatedObject = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()
            );

            Authentication authenticatedObject = authenticationManager.authenticate(unauthenticatedObject);
            SecurityContextHolder.getContext().setAuthentication(authenticatedObject);


            UserDetailsImpl userDetails = (UserDetailsImpl) authenticatedObject.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            List<String> roles = authenticatedObject.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

            UserInfoResponse response = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);

        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", false);
            map.put("message", " Bad request");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/signin/admin")
    public ResponseEntity<?> authenticateAdmin(@RequestBody LoginRequest loginRequest) {

        try {
            Authentication unauthenticatedObject = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()
            );

            Authentication authenticatedObject = authenticationManager.authenticate(unauthenticatedObject);
            SecurityContextHolder.getContext().setAuthentication(authenticatedObject);

            UserDetailsImpl userDetails = (UserDetailsImpl) authenticatedObject.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

            List<String> roles = authenticatedObject.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

            Map<String , Object> map = new HashMap<>();
            map.put("token" , jwtCookie.getValue());
            map.put("roles" , roles);

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(map);

        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", false);
            map.put("message", " Bad request");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/signout")
    public ResponseEntity<?> signout(){
        ResponseCookie cookie = jwtUtils.getCleanCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE , cookie.toString()).body("You have been signed out !!" +
                " kindly login again");
    }


}
