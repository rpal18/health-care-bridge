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

------------------------------------------------------------------------------------------------------------------------------------------------

INSERT INTO Facility (
    id, name, address, type, facility_role, direct_patient_care, facility_status, is24x7, phone_number, email, latitude, longitude
) VALUES
(UUID_GENERATE_V4(), 'Facility 1', 'Address 1', 'HOSPITAL', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567890', 'facility1@example.com', 12.971598, 77.594566),
-- Record 2
(UUID_GENERATE_V4(), 'Facility 2', 'Address 2', 'CLINIC', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567891', 'facility2@example.com', 13.082680, 80.270721),
-- Record 3
(UUID_GENERATE_V4(), 'Facility 3', 'Address 3', 'BLOOD_BANK', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567892', 'facility3@example.com', 28.704060, 77.102493),
-- Record 4
(UUID_GENERATE_V4(), 'Facility 4', 'Address 4', 'TRAUMA_CENTER', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567893', 'facility4@example.com', 19.076090, 72.877426),
-- Record 5
(UUID_GENERATE_V4(), 'Facility 5', 'Address 5', 'NGO', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567894', 'facility5@example.com', 22.572645, 88.363892),
(UUID_GENERATE_V4(), 'Facility 6', 'Address 6', 'HOSPITAL', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567895', 'facility6@example.com', 12.295810, 76.639381),
(UUID_GENERATE_V4(), 'Facility 7', 'Address 7', 'CLINIC', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567896', 'facility7@example.com', 15.317277, 75.713888),
(UUID_GENERATE_V4(), 'Facility 8', 'Address 8', 'BLOOD_BANK', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567897', 'facility8@example.com', 11.016844, 76.955833),
(UUID_GENERATE_V4(), 'Facility 9', 'Address 9', 'TRAUMA_CENTER', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567898', 'facility9@example.com', 10.850516, 76.271083),
(UUID_GENERATE_V4(), 'Facility 10', 'Address 10', 'NGO', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567899', 'facility10@example.com', 9.931233, 76.267304),
(UUID_GENERATE_V4(), 'Facility 11', 'Address 11', 'GOVT_BODY', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567800', 'facility11@example.com', 13.628755, 79.419179),
(UUID_GENERATE_V4(), 'Facility 12', 'Address 12', 'OTHER', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567801', 'facility12@example.com', 17.385044, 78.486671),
(UUID_GENERATE_V4(), 'Facility 13', 'Address 13', 'HOSPITAL', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567802', 'facility13@example.com', 18.520430, 73.856744),
(UUID_GENERATE_V4(), 'Facility 14', 'Address 14', 'CLINIC', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567803', 'facility14@example.com', 19.218331, 72.978090),
(UUID_GENERATE_V4(), 'Facility 15', 'Address 15', 'BLOOD_BANK', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567804', 'facility15@example.com', 21.145800, 79.088155),
(UUID_GENERATE_V4(), 'Facility 16', 'Address 16', 'TRAUMA_CENTER', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567805', 'facility16@example.com', 22.719569, 75.857726),
(UUID_GENERATE_V4(), 'Facility 17', 'Address 17', 'NGO', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567806', 'facility17@example.com', 23.259933, 77.412615),
(UUID_GENERATE_V4(), 'Facility 18', 'Address 18', 'GOVT_BODY', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567807', 'facility18@example.com', 26.912434, 75.787271),
(UUID_GENERATE_V4(), 'Facility 19', 'Address 19', 'OTHER', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567808', 'facility19@example.com', 25.317645, 82.973914),
(UUID_GENERATE_V4(), 'Facility 20', 'Address 20', 'HOSPITAL', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567809', 'facility20@example.com', 27.176670, 78.008075),
(UUID_GENERATE_V4(), 'Facility 21', 'Address 21', 'CLINIC', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567810', 'facility21@example.com', 28.459497, 77.026638),
(UUID_GENERATE_V4(), 'Facility 22', 'Address 22', 'BLOOD_BANK', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567811', 'facility22@example.com', 30.733315, 76.779418),
(UUID_GENERATE_V4(), 'Facility 23', 'Address 23', 'TRAUMA_CENTER', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567812', 'facility23@example.com', 31.104814, 77.173403),
(UUID_GENERATE_V4(), 'Facility 24', 'Address 24', 'NGO', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567813', 'facility24@example.com', 32.726602, 74.857026),
(UUID_GENERATE_V4(), 'Facility 25', 'Address 25', 'GOVT_BODY', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567814', 'facility25@example.com', 34.083656, 74.797371),
(UUID_GENERATE_V4(), 'Facility 26', 'Address 26', 'OTHER', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567815', 'facility26@example.com', 35.244490, 75.728670),
(UUID_GENERATE_V4(), 'Facility 27', 'Address 27', 'HOSPITAL', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567816', 'facility27@example.com', 36.778259, 76.576172),
(UUID_GENERATE_V4(), 'Facility 28', 'Address 28', 'CLINIC', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567817', 'facility28@example.com', 37.774929, 77.419418),
(UUID_GENERATE_V4(), 'Facility 29', 'Address 29', 'BLOOD_BANK', 'RESOURCE_PROVIDER', false, 'BLOCKED', false, '+1234567818', 'facility29@example.com', 38.907192, 77.036871),
(UUID_GENERATE_V4(), 'Facility 30', 'Address 30', 'TRAUMA_CENTER', 'PRIMARY_CARE', true, 'ACTIVE', true, '+1234567819', 'facility30@example.com', 39.904202, 77.102493);
-- Add remaining records similarly...