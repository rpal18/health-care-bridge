package com.Lifelink.HeathCareBridge.seeder;

import com.Lifelink.HeathCareBridge.model.*;
import com.Lifelink.HeathCareBridge.repository.AdminRepository;
import com.Lifelink.HeathCareBridge.repository.FacilityRepository;
import com.Lifelink.HeathCareBridge.repository.ResourceRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final FacilityRepository facilityRepository;
    private final AdminRepository adminRepository;
    private final ResourceRepository resourceRepository;

    public DatabaseSeeder(FacilityRepository facilityRepository,
                          AdminRepository adminRepository,
                          ResourceRepository resourceRepository) {
        this.facilityRepository = facilityRepository;
        this.adminRepository = adminRepository;
        this.resourceRepository = resourceRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Prevent re-seeding if data already exists
        if (facilityRepository.count() > 0) {
            System.out.println("Database already seeded. Skipping...");
            return;
        }

        System.out.println("Starting Database Seeding (200 Facilities, 2000 Resources)...");

        // SRID 4326 is the standard for GPS coordinates (WGS 84)
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Random random = new Random();

        // Kanpur roughly bounds
        double minLat = 26.3500;
        double maxLat = 26.5500;
        double minLon = 80.2500;
        double maxLon = 80.4500;

        List<FacilityType> facilityTypes = List.of(FacilityType.values());
        List<ResourceType> standardResources = Arrays.stream(ResourceType.values())
                .filter(rt -> rt != ResourceType.BLOOD)
                .toList();

        List<Facility> facilitiesToSave = new ArrayList<>();
        List<Admin> adminsToSave = new ArrayList<>();
        List<Resource> resourcesToSave = new ArrayList<>();

        for (int i = 1; i <= 200; i++) {
            // 1. Generate Coordinates (Note: JTS Coordinate takes Longitude first, then Latitude)
            double lat = minLat + (maxLat - minLat) * random.nextDouble();
            double lon = minLon + (maxLon - minLon) * random.nextDouble();
            Point location = geometryFactory.createPoint(new Coordinate(lon, lat));

            // 2. Generate Facility
            Facility facility = new Facility();
            facility.setName("Kanpur Care Center " + i);
            facility.setAddress(i + " Main Road, Kanpur, UP");
            facility.setType(facilityTypes.get(random.nextInt(facilityTypes.size())));
            facility.setFacilityRole(random.nextBoolean() ? FacilityRole.PRIMARY_CARE : FacilityRole.RESOURCE_PROVIDER);
            facility.setRoles(Set.of(facility.getFacilityRole()));
            facility.setDirectPatientCare(random.nextBoolean());
            facility.setFacilityStatus(FacilityStatus.ACTIVE);
            facility.setIs24x7(random.nextBoolean());

            // Matches strict regex: ^(?:\+91|0)?[6-9]\d{9}$
            String phone = "+91" + (9000000000L + random.nextInt(99999999));
            facility.setPhoneNumber(phone);
            facility.setEmail("contact" + i + "@kanpurcare.in");
            facility.setApprovedOn(LocalDateTime.now().minusDays(random.nextInt(365)));
            facility.setLocation(location);

            facilitiesToSave.add(facility);

            // 3. Generate associated Admin
            Admin admin = new Admin();
            admin.setUserName("Admin_" + i);
            admin.setEmail("admin" + i + "@kanpurcare.in");
            admin.setPassword("SecurePass123!");
            admin.setPhoneNumber(phone);
            admin.setFacility(facility);
            // Assuming you have a Role enum representing the admin's authorities
            // admin.setRoles(Set.of(Role.ORG_ADMIN));

            adminsToSave.add(admin);

            // 4. Generate 10 Resources per Facility
            for (int j = 1; j <= 10; j++) {
                boolean isBlood = random.nextInt(10) > 7; // 20% chance to be blood

                Resource resource;
                if (isBlood) {
                    Blood blood = new Blood();
                    blood.setResourceType(ResourceType.BLOOD);
                    blood.setBloodGroup(BloodGroup.values()[random.nextInt(BloodGroup.values().length)]);
                    blood.setBloodComponent(BloodComponent.values()[random.nextInt(BloodComponent.values().length)]);
                    blood.setName(blood.getBloodGroup().name() + " " + blood.getBloodComponent().name());
                    resource = blood;
                } else {
                    resource = new Resource();
                    ResourceType type = standardResources.get(random.nextInt(standardResources.size()));
                    resource.setResourceType(type);
                    resource.setName("Standard " + type.name());
                }

                resource.setQuantity(random.nextInt(50) + 1);
                resource.setAvailable(random.nextBoolean());

                // --- MAP THE CRITICAL FIELDS ---
                resource.setLocation(location); // <-- The new location field added for PostGIS routing
                resource.setFacilityType(facility.getType());
                resource.setFacilityRole(facility.getFacilityRole());
                resource.setFacilityName(facility.getName());
                resource.setFacilityPhoneNumber(facility.getPhoneNumber());
                resource.setFacilityEmail(facility.getEmail());
                resource.setLastUpdated(LocalDateTime.now());

                resourcesToSave.add(resource);
            }
        }

        // 5. Batch Save to Database
        facilityRepository.saveAll(facilitiesToSave);
        adminRepository.saveAll(adminsToSave);
        resourceRepository.saveAll(resourcesToSave);

        System.out.println("Seeding Complete! Added 200 Facilities, 200 Admins, and 2000 Geo-tagged Resources.");
    }
}