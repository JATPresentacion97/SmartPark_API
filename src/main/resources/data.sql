CREATE TABLE PARKING_LOT (
    lot_id VARCHAR(255) PRIMARY KEY,
    location VARCHAR(255),
    capacity INT,
    occupied_spaces INT
);

CREATE TABLE VEHICLE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(20),
    type VARCHAR(20),
    owner_name VARCHAR(100),
    lot_id BIGINT
);

INSERT INTO PARKING_LOT (lot_id, location, capacity, occupied_spaces) VALUES ('LOT001', 'Downtown', 10, 0);
INSERT INTO PARKING_LOT (lot_id, location, capacity, occupied_spaces) VALUES ('LOT002', 'Airport', 20, 0);

INSERT INTO VEHICLE (license_plate, type, owner_name, lot_id) VALUES ('ABC-1234', 'CAR', 'John Doe', NULL);
INSERT INTO VEHICLE (license_plate, type, owner_name, lot_id) VALUES ('XYZ-5678', 'MOTORCYCLE', 'Jane Smith', NULL);
