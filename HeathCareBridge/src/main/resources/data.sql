-- Updated SQL script to insert Resource and Blood records with FacilityRole
INSERT INTO resource (id, name, resource_type, quantity, available, facility_type, facility_role, facility_name, last_updated, dtype, blood_group, blood_component)
VALUES
(UUID_GENERATE_V4(), 'Resource 1', 'OXYGEN', 10, TRUE, 'HOSPITAL', 'PRIMARY_CARE', 'Facility 1', NOW(), 'Resource', NULL, NULL),
(UUID_GENERATE_V4(), 'Resource 2', 'ICU_BED', 20, TRUE, 'CLINIC', 'RESOURCE_PROVIDER', 'Facility 2', NOW(), 'Resource', NULL, NULL),
(UUID_GENERATE_V4(), 'Resource 3', 'VENTILATOR', 30, TRUE, 'BLOOD_BANK', 'PRIMARY_CARE', 'Facility 3', NOW(), 'Resource', NULL, NULL),
(UUID_GENERATE_V4(), 'Blood Resource 1', 'BLOOD', 40, TRUE, 'TRAUMA_CENTER', 'RESOURCE_PROVIDER', 'Facility 4', NOW(), 'BLOOD', 'O_POSITIVE', 'WHOLE_BLOOD'),
(UUID_GENERATE_V4(), 'Blood Resource 2', 'BLOOD', 50, TRUE, 'HOSPITAL', 'PRIMARY_CARE', 'Facility 5', NOW(), 'BLOOD', 'A_NEGATIVE', 'PLATELETS'),
(UUID_GENERATE_V4(), 'Resource 4', 'DEDICATED_COVID_BED', 50, TRUE, 'NGO', 'RESOURCE_PROVIDER', 'Facility 6', NOW(), 'Resource', NULL, NULL),
(UUID_GENERATE_V4(), 'Resource 5', 'COVID_VACCINE', 15, TRUE, 'HOSPITAL', 'PRIMARY_CARE', 'Facility 7', NOW(), 'Resource', NULL, NULL),
(UUID_GENERATE_V4(), 'Resource 6', 'COVID_TEST_KIT', 25, TRUE, 'CLINIC', 'RESOURCE_PROVIDER', 'Facility 8', NOW(), 'Resource', NULL, NULL),
(UUID_GENERATE_V4(), 'Resource 7', 'COVID_MEDICINE', 35, TRUE, 'BLOOD_BANK', 'PRIMARY_CARE', 'Facility 9', NOW(), 'Resource', NULL, NULL),
(UUID_GENERATE_V4(), 'Resource 8', 'COVID_PPE_KIT', 45, TRUE, 'TRAUMA_CENTER', 'RESOURCE_PROVIDER', 'Facility 10', NOW(), 'Resource', NULL, NULL);