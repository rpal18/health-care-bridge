package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "facility_roles", joinColumns = @JoinColumn(name = "facility_id"))
    @Enumerated(EnumType.STRING)
    private Set<FacilityRole> roles;


    @Column(nullable = false)
    private boolean directPatientCare;

    private Boolean is24x7;

    private String phoneNumber;
    private String email;

    private double latitude;
    private double longitude;

}
