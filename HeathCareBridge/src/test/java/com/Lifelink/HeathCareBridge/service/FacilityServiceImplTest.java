package com.Lifelink.HeathCareBridge.service;

import com.Lifelink.HeathCareBridge.exceptions.AlreadyExistsException;
import com.Lifelink.HeathCareBridge.exceptions.DetailsNotFound;
import com.Lifelink.HeathCareBridge.model.*;
import com.Lifelink.HeathCareBridge.payload.FacilityDTO;
import com.Lifelink.HeathCareBridge.payload.FacilityResponseDTO;
import com.Lifelink.HeathCareBridge.repository.AdminRepository;
import com.Lifelink.HeathCareBridge.repository.FacilityRepository;
import com.Lifelink.HeathCareBridge.repository.RequestedFacilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityServiceImplTest {

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private RequestedFacilityRepository requestedFacilityRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    private Facility facility;
    private FacilityDTO facilityDTO;
    private FacilityResponseDTO facilityResponseDTO;
    private RequestedFacility requestedFacility;
    private Admin admin;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @BeforeEach
    void setUp() {

        Point point = geometryFactory.createPoint(new Coordinate(80.3319, 26.4499));

        facility = new Facility();
        facility.setId(UUID.randomUUID());
        facility.setName("City Hospital");
        facility.setAddress("Kanpur");
        facility.setType(FacilityType.HOSPITAL);
        facility.setFacilityRole(FacilityRole.PRIMARY_CARE);
        facility.setFacilityStatus(FacilityStatus.ACTIVE);
        facility.setPhoneNumber("9876543210");
        facility.setEmail("hospital@test.com");
        facility.setLocation(point);

        facilityDTO = new FacilityDTO();
        facilityDTO.setName("City Hospital");
        facilityDTO.setAddress("Kanpur");
        facilityDTO.setType(FacilityType.HOSPITAL);
        facilityDTO.setFacilityRole(FacilityRole.PRIMARY_CARE);
        facilityDTO.setRoles(Set.of(FacilityRole.PRIMARY_CARE));
        facilityDTO.setDirectPatientCare(true);
        facilityDTO.setIs24x7(true);
        facilityDTO.setPhoneNumber("9876543210");
        facilityDTO.setEmail("hospital@test.com");
        facilityDTO.setLatitude(26.4499);
        facilityDTO.setLongitude(80.3319);

        facilityResponseDTO = new FacilityResponseDTO();
        facilityResponseDTO.setId(facility.getId());
        facilityResponseDTO.setName(facility.getName());

        requestedFacility = new RequestedFacility();
        requestedFacility.setId(UUID.randomUUID());
        requestedFacility.setName("Requested Hospital");
        requestedFacility.setAddress("Kanpur");
        requestedFacility.setType(FacilityType.HOSPITAL);
        requestedFacility.setFacilityRole(FacilityRole.PRIMARY_CARE);
        requestedFacility.setFacilityStatus(FacilityStatus.PENDING);
        requestedFacility.setPhoneNumber("9999999999");
        requestedFacility.setEmail("request@test.com");
        requestedFacility.setLatitude(26.44);
        requestedFacility.setLongitude(80.33);

        admin = new Admin();
        admin.setId(UUID.randomUUID());
    }

    @Test
    void approveFacility_ShouldApproveSuccessfully() {

        when(requestedFacilityRepository.findById(requestedFacility.getId()))
                .thenReturn(Optional.of(requestedFacility));

        when(facilityRepository.findFacilityByPhoneNumberOrEmail(
                requestedFacility.getPhoneNumber(),
                requestedFacility.getEmail()))
                .thenReturn(null);

        when(facilityRepository.save(any(Facility.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(modelMapper.map(any(Facility.class), eq(FacilityResponseDTO.class)))
                .thenReturn(facilityResponseDTO);

        FacilityResponseDTO response =
                facilityService.approveFacility(requestedFacility.getId());

        assertNotNull(response);

        verify(requestedFacilityRepository).delete(requestedFacility);
        verify(facilityRepository).save(any(Facility.class));
    }

    @Test
    void approveFacility_ShouldThrowException_WhenRequestNotFound() {

        UUID id = UUID.randomUUID();

        when(requestedFacilityRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(DetailsNotFound.class,
                () -> facilityService.approveFacility(id));
    }

    @Test
    void approveFacility_ShouldThrowException_WhenFacilityAlreadyExists() {

        when(requestedFacilityRepository.findById(requestedFacility.getId()))
                .thenReturn(Optional.of(requestedFacility));

        when(facilityRepository.findFacilityByPhoneNumberOrEmail(any(), any()))
                .thenReturn(facility);

        assertThrows(AlreadyExistsException.class,
                () -> facilityService.approveFacility(requestedFacility.getId()));
    }

    @Test
    void getFacilityById_ShouldReturnFacility() {

        when(facilityRepository.findById(facility.getId()))
                .thenReturn(Optional.of(facility));

        when(modelMapper.map(facility, FacilityResponseDTO.class))
                .thenReturn(facilityResponseDTO);

        FacilityResponseDTO response =
                facilityService.getFacilityById(facility.getId());

        assertNotNull(response);
        assertEquals(facility.getName(), response.getName());
    }

    @Test
    void getFacilityById_ShouldThrowException_WhenNotFound() {

        UUID id = UUID.randomUUID();

        when(facilityRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(DetailsNotFound.class,
                () -> facilityService.getFacilityById(id));
    }

    @Test
    void blockFacility_ShouldBlockSuccessfully() {

        when(facilityRepository.findById(facility.getId()))
                .thenReturn(Optional.of(facility));

        String response = facilityService.blockFacility(facility.getId());

        assertEquals(FacilityStatus.BLOCKED, facility.getFacilityStatus());

        verify(facilityRepository).save(facility);

        assertTrue(response.contains("blocked successfully"));
    }

    @Test
    void getAllFacilities_ShouldReturnFacilities() {

        Page<Facility> facilityPage =
                new PageImpl<>(List.of(facility));

        when(facilityRepository.findAll(any(Pageable.class)))
                .thenReturn(facilityPage);

        when(modelMapper.map(any(Facility.class), eq(FacilityResponseDTO.class)))
                .thenReturn(facilityResponseDTO);

        List<FacilityResponseDTO> response =
                facilityService.getAllFacilities(0, 10);

        assertEquals(1, response.size());
    }

    @Test
    void getAllFacilities_ShouldThrowException_WhenEmpty() {

        Page<Facility> emptyPage =
                new PageImpl<>(List.of());

        when(facilityRepository.findAll(any(Pageable.class)))
                .thenReturn(emptyPage);

        assertThrows(DetailsNotFound.class,
                () -> facilityService.getAllFacilities(0, 10));
    }

    @Test
    void deleteFacility_ShouldSoftDeleteSuccessfully() {

        when(facilityRepository.findByIdAndIsDeletedFalse(facility.getId()))
                .thenReturn(Optional.of(facility));

        String response =
                facilityService.deleteFacility(facility.getId(), "Violation");

        assertTrue(facility.isDeleted());

        verify(facilityRepository).save(facility);

        assertTrue(response.contains("soft deleted successfully"));
    }

    @Test
    void restoreFacility_ShouldRestoreSuccessfully() {

        facility.setDeleted(true);

        when(facilityRepository.findById(facility.getId()))
                .thenReturn(Optional.of(facility));

        String response =
                facilityService.restoreFacility(facility.getId());

        assertFalse(facility.isDeleted());

        verify(facilityRepository).save(facility);

        assertTrue(response.contains("restored successfully"));
    }

    @Test
    void restoreFacility_ShouldReturnAlreadyActiveMessage() {

        facility.setDeleted(false);

        when(facilityRepository.findById(facility.getId()))
                .thenReturn(Optional.of(facility));

        String response =
                facilityService.restoreFacility(facility.getId());

        assertEquals("Facility is already active.", response);

        verify(facilityRepository, never()).save(any());
    }

    @Test
    void requestFacility_ShouldRequestSuccessfully() {

        UUID userId = UUID.randomUUID();

        when(facilityRepository.findFacilityByPhoneNumberOrEmail(any(), any()))
                .thenReturn(null);

        when(requestedFacilityRepository.findByPhoneNumberOrEmail(any(), any()))
                .thenReturn(null);

        when(requestedFacilityRepository.save(any(RequestedFacility.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(modelMapper.map(any(RequestedFacility.class),
                eq(FacilityResponseDTO.class)))
                .thenReturn(facilityResponseDTO);

        FacilityResponseDTO response =
                facilityService.requestFacility(facilityDTO, userId);

        assertNotNull(response);

        verify(requestedFacilityRepository).save(any(RequestedFacility.class));
    }

    @Test
    void requestFacility_ShouldThrowException_WhenFacilityExists() {

        when(facilityRepository.findFacilityByPhoneNumberOrEmail(any(), any()))
                .thenReturn(facility);

        assertThrows(AlreadyExistsException.class,
                () -> facilityService.requestFacility(
                        facilityDTO,
                        UUID.randomUUID()
                ));
    }

    @Test
    void rejectFacilityRequest_ShouldRejectSuccessfully() {

        when(requestedFacilityRepository.findById(requestedFacility.getId()))
                .thenReturn(Optional.of(requestedFacility));

        String response =
                facilityService.rejectFacilityRequest(requestedFacility.getId());

        assertEquals(FacilityStatus.REJECTED,
                requestedFacility.getFacilityStatus());

        assertTrue(response.contains("Facility request with id"));
    }

    @Test
    void assignFacilityAdmin_ShouldAssignSuccessfully() {

        UUID facilityId = facility.getId();
        UUID adminId = admin.getId();

        when(facilityRepository.findById(facilityId))
                .thenReturn(Optional.of(facility));

        when(adminRepository.findById(adminId))
                .thenReturn(Optional.of(admin));

        String response =
                facilityService.assignFacilityAdmin(facilityId, adminId);

        assertEquals(facility, admin.getFacility());

        verify(adminRepository).save(admin);
        verify(facilityRepository).save(facility);

        assertTrue(response.contains("assigned"));
    }

    @Test
    void getFacilityDetailsForOrgAdmin_ShouldReturnFacilityDetails() {

        admin.setFacility(facility);

        when(adminRepository.findById(admin.getId()))
                .thenReturn(Optional.of(admin));

        when(modelMapper.map(facility, FacilityResponseDTO.class))
                .thenReturn(facilityResponseDTO);

        FacilityResponseDTO response =
                facilityService.getFacilityDetailsForOrgAdmin(admin);

        assertNotNull(response);
    }

    @Test
    void getFacilityDetailsForOrgAdmin_ShouldThrowException_WhenNoFacilityAssigned() {

        admin.setFacility(null);

        when(adminRepository.findById(admin.getId()))
                .thenReturn(Optional.of(admin));

        assertThrows(DetailsNotFound.class,
                () -> facilityService.getFacilityDetailsForOrgAdmin(admin));
    }
}