package com.Lifelink.HeathCareBridge.util;

import com.Lifelink.HeathCareBridge.model.User;
import com.Lifelink.HeathCareBridge.repository.UserRepository;
import com.Lifelink.HeathCareBridge.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class  AuthUtil {

    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String loggedInEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails =  (UserDetailsImpl)authentication.getPrincipal();
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(()->
                new UsernameNotFoundException("User not found !!"));
        return user.getEmail();
    }

    public User loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails =  (UserDetailsImpl)authentication.getPrincipal();
        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(()->
                new UsernameNotFoundException("User not found !!"));

        return user;
    }

}

