package com.Lifelink.HeathCareBridge.bootstrap;

import com.Lifelink.HeathCareBridge.exceptions.AlreadyExistsException;
import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.model.Role;
import com.Lifelink.HeathCareBridge.model.User;
import com.Lifelink.HeathCareBridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Value("${adminEmail}")
    private String adminEmail;
    @Value("${adminPassword}")
    private String adminPassword;
    @Value("${adminUsername}")
    private String adminUserName;
    @Value("${phoneNumber}")
    private String phoneNumber;

    @Override
    public void run(String... args) throws Exception {
        if(adminEmail == null  || adminPassword == null || adminUserName == null){
            throw new DetailsNotFound("Admin email or password  or user name not found in environment variables");
        }
        if(userRepository.existsByEmail(adminEmail) || userRepository.existsByUserName(adminUserName)){
           throw new AlreadyExistsException(" admin email or password already exists!!");

        }
        User superAdmin = new User();
        superAdmin.setUserName(adminUserName);
        superAdmin.setEmail(adminEmail);
        superAdmin.setPassword(passwordEncoder.encode(adminPassword));
        superAdmin.setPhoneNumber(phoneNumber);
        superAdmin.setRoles(Set.of(Role.SYSTEM_ADMIN));
        userRepository.save(superAdmin);

    }
}
