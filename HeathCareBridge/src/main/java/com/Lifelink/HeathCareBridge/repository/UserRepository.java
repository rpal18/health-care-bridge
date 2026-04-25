package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserName(String username);


    boolean existsByEmail(String adminEmail);

    boolean existsByUserName(String adminUserName);
}