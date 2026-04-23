package com.Lifelink.HeathCareBridge.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="resource_type", discriminatorType=DiscriminatorType.STRING)
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private int quantity;

    private boolean available;

    private FacilityType facilityType;

    private String facilityName;

    private LocalDateTime lastUpdated;

}
