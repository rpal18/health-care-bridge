package com.Lifelink.HeathCareBridge.controller;

import com.Lifelink.HeathCareBridge.payload.LoginRequest;
import com.Lifelink.HeathCareBridge.payload.UserInfoResponse;
import com.Lifelink.HeathCareBridge.security.JwtUtils;
import com.Lifelink.HeathCareBridge.security.UserDetailsImpl;
import com.Lifelink.HeathCareBridge.service.UserService;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;

    private UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;

    }

   private Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        /*
        step by step flow :
        1) we extract username and password from loginRequest
        2) create a Authentication type object , that is unauthenticated initially
        3) after that we authenticate the Unauthenticated object ( by making use of AuthenticationManager )
        4) After that , we save this authenticated object into spring context so that we do not need for authentication
        with each request .
         */

        System.out.println("1. Login Hit!");
        System.out.println("2. Receiving Username: " + loginRequest.getUserName());
        System.out.println("3. Receiving Password: " + loginRequest.getPassword());

        try {
            Authentication unauthenticatedObject = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()
            );

            Authentication authenticatedObject = authenticationManager.authenticate(unauthenticatedObject);
            logger.info("Authentication successful for user: {}", loginRequest.getUserName());
            logger.info("Authenticated Object: {}", authenticatedObject);
            SecurityContextHolder.getContext().setAuthentication(authenticatedObject);
            /*
            Now we need to generate Jwt authentication token , as we need to send this with every request
            that will be validated against authenticated object saved security context .
             */

            UserDetailsImpl userDetails = (UserDetailsImpl) authenticatedObject.getPrincipal();

            //String jwtToken = jwtUtils.generateTokenFromUserName(userDetails);
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
}
