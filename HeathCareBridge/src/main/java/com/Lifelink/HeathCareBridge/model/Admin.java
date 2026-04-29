package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    @OneToOne
    @JoinColumn(name = "id")
    private Facility facility;

    private final Role role = Role.ORG_ADMIN;

    public Admin() {
    }

    public Admin(Facility facility) {
        this.facility = facility;
    }

    public Admin(UUID id, String name, String email, String password, String phoneNumber, Set<Role> roles, Facility facility) {
        super(id, name, email, password, phoneNumber, roles);
        this.facility = facility;
    }

    public Facility getFacility() {
        return facility;
    }
    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}