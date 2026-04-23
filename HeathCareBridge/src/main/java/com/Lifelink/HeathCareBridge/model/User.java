package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;

    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    @ElementCollection(targetClass = Role.class , fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles" , joinColumns = @JoinColumn( name = "user_id"))
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

}
