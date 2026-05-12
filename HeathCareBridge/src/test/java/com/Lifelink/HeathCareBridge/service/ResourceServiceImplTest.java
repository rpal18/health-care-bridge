package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.exceptions.IllegalArgument;
import com.Lifelink.HeathCareBridge.model.*;
import com.Lifelink.HeathCareBridge.payload.*;
import com.Lifelink.HeathCareBridge.projection.FacilityLocationProjection;
import com.Lifelink.HeathCareBridge.repository.BloodRepository;
import com.Lifelink.HeathCareBridge.repository.FacilityRepository;
import com.Lifelink.HeathCareBridge.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.*;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private BloodRepository bloodRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private Facility facility;
    private Admin admin;
    private Resource resource;
    private ResourceDTO resourceDTO;
    private ResourceResponseDTO resourceResponseDTO;

    @BeforeEach
    void setUp() {

        facility = new Facility();
        facility.setId(UUID.randomUUID());
        facility.setName("City Hospital");
        facility.setType(FacilityType.HOSPITAL);
        facility.setFacilityRole(FacilityRole.RESOURCE_PROVIDER);
        facility.setEmail("hospital@test.com");
        facility.setPhoneNumber("9876543210");
        facility.setLocation(createPoint(80.3319, 26.4499));

        admin = new Admin();
        admin.setFacility(facility);

        resource = new Resource();
        resource.setId(UUID.randomUUID());
        resource.setName("Ventilator");
        resource.setQuantity(10);
        resource.setAvailable(true);
        resource.setResourceType(ResourceType.VENTILATOR);
        resource.setFacilityName(facility.getName());
        resource.setFacilityType(facility.getType());
        resource.setFacilityRole(facility.getFacilityRole());
        resource.setFacilityEmail(facility.getEmail());
        resource.setFacilityPhoneNumber(facility.getPhoneNumber());
        resource.setLocation(facility.getLocation());
        resource.setLastUpdated(LocalDateTime.now());

        resourceDTO = new ResourceDTO(
                ResourceType.VENTILATOR,
                "Ventilator",
                5
        );

        resourceResponseDTO = new ResourceResponseDTO();
        resourceResponseDTO.setId(resource.getId());
        resourceResponseDTO.setName(resource.getName());
        resourceResponseDTO.setQuantity(resource.getQuantity());
    }

    @Nested
    class AddResourceTests {

        @Test
        void shouldAddNewResourceSuccessfully() {

            when(resourceRepository.findByNameAndFacilityNameAndFacilityTypeAndResourceType(
                    anyString(),
                    anyString(),
                    any(),
                    any()
            )).thenReturn(null);

            when(resourceRepository.save(any(Resource.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(modelMapper.map(any(Resource.class), eq(ResourceResponseDTO.class)))
                    .thenReturn(resourceResponseDTO);

            ResourceResponseDTO response =
                    resourceService.addResource(resourceDTO, admin);

            assertNotNull(response);

            verify(resourceRepository).save(any(Resource.class));
            verify(modelMapper).map(any(Resource.class), eq(ResourceResponseDTO.class));
        }

        @Test
        void shouldUpdateExistingResourceSuccessfully() {

            Resource existingResource = new Resource();
            existingResource.setQuantity(10);

            when(resourceRepository.findByNameAndFacilityNameAndFacilityTypeAndResourceType(
                    anyString(),
                    anyString(),
                    any(),
                    any()
            )).thenReturn(existingResource);

            when(resourceRepository.save(any(Resource.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(modelMapper.map(any(Resource.class), eq(ResourceResponseDTO.class)))
                    .thenReturn(resourceResponseDTO);

            resourceService.addResource(resourceDTO, admin);

            assertEquals(15, existingResource.getQuantity());

            verify(resourceRepository).save(existingResource);
        }

        @Test
        void shouldThrowExceptionWhenAdminHasNoFacility() {

            admin.setFacility(null);

            DetailsNotFound exception = assertThrows(
                    DetailsNotFound.class,
                    () -> resourceService.addResource(resourceDTO, admin)
            );

            assertEquals(
                    "Admin is not associated with any facility",
                    exception.getMessage()
            );

            verify(resourceRepository, never()).save(any());
        }

        @Test
        void shouldThrowExceptionWhenQuantityIsNegative() {

            resourceDTO.setQuantity(-5);

            IllegalArgument exception = assertThrows(
                    IllegalArgument.class,
                    () -> resourceService.addResource(resourceDTO, admin)
            );

            assertEquals(
                    "Quantity to add cannot be negative",
                    exception.getMessage()
            );

            verify(resourceRepository, never()).save(any());
        }
    }

    @Nested
    class GetAllResourceTests {

        @Test
        void shouldReturnAllResourcesSuccessfully() {

            when(resourceRepository.findAll())
                    .thenReturn(List.of(resource));

            when(modelMapper.map(any(Resource.class), eq(ResourceResponseDTO.class)))
                    .thenAnswer(invocation -> {
                        Resource res = invocation.getArgument(0);

                        ResourceResponseDTO dto = new ResourceResponseDTO();
                        dto.setId(res.getId());
                        dto.setName(res.getName());
                        dto.setQuantity(res.getQuantity());

                        return dto;
                    });

            List<ResourceResponseDTO> response =
                    resourceService.getAllResource();

            assertEquals(1, response.size());

            ResourceResponseDTO dto = response.get(0);

            assertEquals("Ventilator", dto.getName());
            assertEquals(26.4499, dto.getLatitude());
            assertEquals(80.3319, dto.getLongitude());

            verify(resourceRepository).findAll();
        }

        @Test
        void shouldReturnEmptyListWhenNoResourcesFound() {

            when(resourceRepository.findAll())
                    .thenReturn(List.of());

            List<ResourceResponseDTO> response =
                    resourceService.getAllResource();

            assertTrue(response.isEmpty());
        }
    }

    @Nested
    class AddBloodResourceTests {

        @Test
        void shouldAddBloodResourceSuccessfully() {

            BloodResourceDTO dto = createBloodResourceDTO();

            Blood blood = createBloodResource();

            BloodResourceResponseDTO responseDTO =
                    new BloodResourceResponseDTO();

            when(bloodRepository.findByNameAndFacilityNameAndFacilityTypeAndResourceTypeAndBloodComponent(
                    anyString(),
                    anyString(),
                    any(),
                    any(),
                    any()
            )).thenReturn(null);

            when(bloodRepository.save(any(Blood.class)))
                    .thenReturn(blood);

            when(modelMapper.map(any(Blood.class), eq(BloodResourceResponseDTO.class)))
                    .thenReturn(responseDTO);

            BloodResourceResponseDTO response =
                    resourceService.addBloodResource(dto, admin);

            assertNotNull(response);

            verify(bloodRepository).save(any(Blood.class));
        }

        @Test
        void shouldThrowExceptionWhenBloodQuantityIsNegative() {

            BloodResourceDTO dto = createBloodResourceDTO();
            dto.setQuantity(-10);

            IllegalArgument exception = assertThrows(
                    IllegalArgument.class,
                    () -> resourceService.addBloodResource(dto, admin)
            );

            assertEquals(
                    "Quantity to add cannot be negative",
                    exception.getMessage()
            );

            verify(bloodRepository, never()).save(any());
        }
    }

    @Nested
    class UpdateResourceQuantityTests {

        @Test
        void shouldUpdateResourceQuantitySuccessfully() {

            when(resourceRepository.findById(resource.getId()))
                    .thenReturn(Optional.of(resource));

            when(resourceRepository.save(any(Resource.class)))
                    .thenReturn(resource);

            when(modelMapper.map(any(Resource.class), eq(ResourceResponseDTO.class)))
                    .thenReturn(resourceResponseDTO);

            ResourceResponseDTO response =
                    resourceService.updateResourceQuantity(
                            resource.getId(),
                            5,
                            admin
                    );

            assertNotNull(response);
            assertEquals(15, resource.getQuantity());

            verify(resourceRepository).save(resource);
        }

        @Test
        void shouldThrowExceptionWhenResourceNotFound() {

            UUID id = UUID.randomUUID();

            when(resourceRepository.findById(id))
                    .thenReturn(Optional.empty());

            assertThrows(
                    DetailsNotFound.class,
                    () -> resourceService.updateResourceQuantity(id, 5, admin)
            );

            verify(resourceRepository, never()).save(any());
        }

        @Test
        void shouldThrowExceptionWhenUpdatedQuantityBecomesNegative() {

            resource.setQuantity(2);

            when(resourceRepository.findById(resource.getId()))
                    .thenReturn(Optional.of(resource));

            IllegalArgument exception = assertThrows(
                    IllegalArgument.class,
                    () -> resourceService.updateResourceQuantity(
                            resource.getId(),
                            -10,
                            admin
                    )
            );

            assertEquals(
                    "Quantity to add cannot be negative",
                    exception.getMessage()
            );
        }
    }

    @Nested
    class AllocateResourceTests {

        @Test
        void shouldAllocateResourceSuccessfully() {

            Facility sameFacility = new Facility();
            sameFacility.setId(facility.getId());

            when(resourceRepository.findById(resource.getId()))
                    .thenReturn(Optional.of(resource));

            when(facilityRepository.findFacilityByEmailAndPhoneNumber(
                    anyString(),
                    anyString()
            )).thenReturn(Optional.of(sameFacility));

            when(resourceRepository.save(any(Resource.class)))
                    .thenReturn(resource);

            int remainingQuantity =
                    resourceService.allocateResource(
                            resource.getId(),
                            admin,
                            3
                    );

            assertEquals(7, remainingQuantity);

            verify(resourceRepository).save(resource);
        }

        @Test
        void shouldThrowExceptionWhenResourceNotFound() {

            UUID id = UUID.randomUUID();

            when(resourceRepository.findById(id))
                    .thenReturn(Optional.empty());

            assertThrows(
                    DetailsNotFound.class,
                    () -> resourceService.allocateResource(id, admin, 2)
            );
        }

        @Test
        void shouldThrowExceptionWhenQuantityIsNegative() {

            when(resourceRepository.findById(resource.getId()))
                    .thenReturn(Optional.of(resource));

            when(facilityRepository.findFacilityByEmailAndPhoneNumber(
                    anyString(),
                    anyString()
            )).thenReturn(Optional.of(facility));

            IllegalArgument exception = assertThrows(
                    IllegalArgument.class,
                    () -> resourceService.allocateResource(
                            resource.getId(),
                            admin,
                            -5
                    )
            );

            assertEquals(
                    "Quantity to allocate cannot be negative",
                    exception.getMessage()
            );
        }

        @Test
        void shouldThrowExceptionWhenQuantityExceedsAvailableStock() {

            when(resourceRepository.findById(resource.getId()))
                    .thenReturn(Optional.of(resource));

            when(facilityRepository.findFacilityByEmailAndPhoneNumber(
                    anyString(),
                    anyString()
            )).thenReturn(Optional.of(facility));

            IllegalArgument exception = assertThrows(
                    IllegalArgument.class,
                    () -> resourceService.allocateResource(
                            resource.getId(),
                            admin,
                            100
                    )
            );

            assertEquals(
                    "Not enough quantity available to allocate",
                    exception.getMessage()
            );
        }

        @Test
        void shouldThrowAccessDeniedExceptionWhenFacilityDoesNotMatch() {

            Facility differentFacility = new Facility();
            differentFacility.setId(UUID.randomUUID());

            when(resourceRepository.findById(resource.getId()))
                    .thenReturn(Optional.of(resource));

            when(facilityRepository.findFacilityByEmailAndPhoneNumber(
                    anyString(),
                    anyString()
            )).thenReturn(Optional.of(differentFacility));

            assertThrows(
                    AccessDeniedException.class,
                    () -> resourceService.allocateResource(
                            resource.getId(),
                            admin,
                            2
                    )
            );
        }
    }

    @Nested
    class GetFacilitiesWhereResourceIsAvailableTests {

        @Test
        void shouldReturnNearbyFacilitiesForNormalResources() {

            ResourceRequestDTO dto = new ResourceRequestDTO();
            dto.setLatitude(26.4499);
            dto.setLongitude(80.3319);
            dto.setResourceTypes(List.of(ResourceType.VENTILATOR));

            FacilityLocationProjection projection =
                    mock(FacilityLocationProjection.class);

            when(projection.getFacilityName())
                    .thenReturn("City Hospital");

            when(projection.getLatitude())
                    .thenReturn(26.4499);

            when(projection.getLongitude())
                    .thenReturn(80.3319);

            when(projection.getDistance())
                    .thenReturn(5000.0);

            when(resourceRepository.findTop10NearestFacilityLocations(
                    anyList(),
                    anyDouble(),
                    anyDouble()
            )).thenReturn(List.of(projection));

            List<NearByResponseDTO> response =
                    resourceService.getFacilitiesWhereResourceIsAvailable(dto);

            assertEquals(1, response.size());

            NearByResponseDTO result = response.get(0);

            assertEquals("City Hospital", result.getFacilityName());
            assertEquals(5.0, result.getDistance());
        }

        @Test
        void shouldReturnNearbyFacilitiesForBloodResources() {

            ResourceRequestDTO dto = new ResourceRequestDTO();

            dto.setLatitude(26.4499);
            dto.setLongitude(80.3319);
            dto.setResourceTypes(List.of(ResourceType.BLOOD));
            dto.setBloodGroup(BloodGroup.O_POSITIVE);
            dto.setBloodComponent(BloodComponent.WHOLE_BLOOD);

            FacilityLocationProjection projection =
                    mock(FacilityLocationProjection.class);

            when(resourceRepository.findTop10NearestBloodFacilityLocations(
                    anyList(),
                    anyDouble(),
                    anyDouble(),
                    anyString(),
                    anyString()
            )).thenReturn(List.of(projection));

            List<NearByResponseDTO> response =
                    resourceService.getFacilitiesWhereResourceIsAvailable(dto);

            assertEquals(1, response.size());

            verify(resourceRepository)
                    .findTop10NearestBloodFacilityLocations(
                            anyList(),
                            anyDouble(),
                            anyDouble(),
                            anyString(),
                            anyString()
                    );
        }

        @Test
        void shouldThrowExceptionWhenResourceTypesAreEmpty() {

            ResourceRequestDTO dto = new ResourceRequestDTO();
            dto.setResourceTypes(List.of());

            IllegalArgument exception = assertThrows(
                    IllegalArgument.class,
                    () -> resourceService.getFacilitiesWhereResourceIsAvailable(dto)
            );

            assertEquals(
                    "Resource types cannot be null or empty",
                    exception.getMessage()
            );
        }
    }

    private BloodResourceDTO createBloodResourceDTO() {

        BloodResourceDTO dto = new BloodResourceDTO();

        dto.setName("Blood Unit");
        dto.setQuantity(5);
        dto.setResourceType(ResourceType.BLOOD);
        dto.setBloodGroup(BloodGroup.O_POSITIVE);
        dto.setBloodComponent(BloodComponent.WHOLE_BLOOD);

        return dto;
    }

    private Blood createBloodResource() {

        Blood blood = new Blood();

        blood.setId(UUID.randomUUID());
        blood.setName("Blood Unit");
        blood.setQuantity(5);
        blood.setResourceType(ResourceType.BLOOD);
        blood.setBloodGroup(BloodGroup.O_POSITIVE);
        blood.setBloodComponent(BloodComponent.WHOLE_BLOOD);

        return blood;
    }

    private Point createPoint(double longitude, double latitude) {

        GeometryFactory geometryFactory =
                new GeometryFactory(new PrecisionModel(), 4326);

        return geometryFactory.createPoint(
                new Coordinate(longitude, latitude)
        );
    }
}