-- Create Institution with UUID consisting of all zeros
INSERT INTO Institutions (Id, tenant_id, name, email, uen, address, contact_number, created_at, updated_at)
VALUES
('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'First Institution', 'first@institution.com', 'UEN12345A', '123 First Street, Singapore', '+65 1234 5678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Create Institution with UUID ending with 1
INSERT INTO Institutions (Id, tenant_id, name, email, uen, address, contact_number, created_at, updated_at)
VALUES
('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Second Institution', 'second@institution.com', 'UEN67890B', '456 Second Avenue, Singapore', '+65 9876 5432', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);