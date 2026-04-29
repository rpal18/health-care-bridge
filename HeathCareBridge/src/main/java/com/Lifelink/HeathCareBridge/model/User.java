package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role_type", discriminatorType = DiscriminatorType.STRING)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;
    @NotBlank(message = "Name is required")
    private String userName;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(?:\\+91|0)?[6-9]\\d{9}$", message = "Phone number should be valid")
    private String phoneNumber;

    @ElementCollection(targetClass = Role.class , fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles" , joinColumns = @JoinColumn( name = "user_id"))
    @Enumerated(value = EnumType.STRING)
    private Set<Role> allRoles;

    public User() {
    }

    public User(UUID id, String name, String email, String password, String phoneNumber, Set<Role> roles) {
        this.id = id;
        this.userName = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.allRoles = roles;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Role> getRoles() {
        return allRoles;
    }

    public void setRoles(Set<Role> roles) {
        this.allRoles = roles;
    }

}
