//package com.Lifelink.HeathCareBridge.bootstrap;
//
//import com.Lifelink.HeathCareBridge.exceptions.AlreadyExistsException;
//import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
//import com.Lifelink.HeathCareBridge.model.Role;
//import com.Lifelink.HeathCareBridge.model.User;
//import com.Lifelink.HeathCareBridge.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//
//@Component
//public class AdminSeeder implements CommandLineRunner {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserRepository userRepository;
//
//
////    @Override
////    public void run(String... args) throws Exception {
////        String adminUserName = System.getenv("adminUsername");
////        String adminEmail = System.getenv("adminEmail");
////        String adminPassword = System.getenv("adminPassword");
////        String adminPhoneNumber = System.getenv("adminPhoneNumber");
////        if(adminEmail == null  || adminPassword == null || adminUserName == null){
////            throw new DetailsNotFound("Admin email or password  or user name not found in environment variables");
////        }
////        if(userRepository.existsByEmail(adminEmail) || userRepository.existsByUserName(adminUserName)){
////           throw new AlreadyExistsException(" admin email or password already exists!!");
////
////        }
////        User superAdmin = new User();
////        superAdmin.setUserName(adminUserName);
////        superAdmin.setEmail(adminEmail);
////        superAdmin.setPassword(passwordEncoder.encode(adminPassword));
////        superAdmin.setPhoneNumber(adminPhoneNumber);
////        superAdmin.setRoles(Set.of(Role.SYSTEM_ADMIN));
////        userRepository.save(superAdmin);
////
////    }
//}
