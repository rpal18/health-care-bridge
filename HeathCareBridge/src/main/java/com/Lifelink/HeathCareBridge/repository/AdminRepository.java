package com.Lifelink.HeathCareBridge.repository;

import com.Lifelink.HeathCareBridge.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Admin findAdminByPhoneNumberOrEmail(String phoneNumber, String email);
}