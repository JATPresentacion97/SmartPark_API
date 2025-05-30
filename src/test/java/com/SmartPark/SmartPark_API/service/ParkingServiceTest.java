package com.SmartPark.SmartPark_API.service;

import com.SmartPark.SmartPark_API.model.ParkingLot;
import com.SmartPark.SmartPark_API.model.Vehicle;
import com.SmartPark.SmartPark_API.model.VehicleType;
import com.SmartPark.SmartPark_API.repository.ParkingLotRepository;
import com.SmartPark.SmartPark_API.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingServiceTest {

    private ParkingLotRepository parkingLotRepository;
    private VehicleRepository vehicleRepository;
    private ParkingService parkingService;

    @BeforeEach
    void setUp() {
        parkingLotRepository = mock(ParkingLotRepository.class);
        vehicleRepository = mock(VehicleRepository.class);
        parkingService = new ParkingService(parkingLotRepository, vehicleRepository);
    }

    @Test
    void testRegisterParkingLot() {
        ParkingLot lot = new ParkingLot("LOT1", "Downtown", 5);
        when(parkingLotRepository.save(lot)).thenReturn(lot);

        ParkingLot saved = parkingService.registerParkingLot(lot);

        assertEquals("LOT1", saved.getLotId());
        verify(parkingLotRepository, times(1)).save(lot);
    }

    @Test
    void testRegisterVehicle() {
        Vehicle vehicle = new Vehicle("ABC-123", VehicleType.CAR, "John Doe");
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle saved = parkingService.registerVehicle(vehicle);

        assertEquals("ABC-123", saved.getLicensePlate());
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void testCheckInVehicleSuccessfully() {
        ParkingLot lot = new ParkingLot("LOT1", "Downtown", 1);
        Vehicle vehicle = new Vehicle("ABC-123", VehicleType.CAR, "John Doe");

        when(parkingLotRepository.findById("LOT1")).thenReturn(Optional.of(lot));
        when(vehicleRepository.findById("ABC-123")).thenReturn(Optional.of(vehicle));
        when(parkingLotRepository.save(any())).thenReturn(lot);
        when(vehicleRepository.save(any())).thenReturn(vehicle);

        String result = parkingService.checkInVehicle("LOT1", "ABC-123");

        assertEquals("Vehicle checked in successfully", result);  // updated to match service return
        assertEquals(lot, vehicle.getParkingLot());
        assertEquals(1, lot.getOccupiedSpaces());

        verify(vehicleRepository, times(1)).save(vehicle);
        verify(parkingLotRepository, times(1)).save(lot);
    }

    @Test
    void testCheckInVehicleFailsIfLotFull() {
        ParkingLot lot = new ParkingLot("LOT1", "Downtown", 1);
        lot.setOccupiedSpaces(1); // lot is full
        Vehicle vehicle = new Vehicle("ABC-123", VehicleType.CAR, "John Doe");

        when(parkingLotRepository.findById("LOT1")).thenReturn(Optional.of(lot));
        when(vehicleRepository.findById("ABC-123")).thenReturn(Optional.of(vehicle));

        String result = parkingService.checkInVehicle("LOT1", "ABC-123");

        assertEquals("Parking lot is full", result);  // check returned message, not exception
    }

    @Test
    void testCheckOutVehicleSuccessfully() {
        ParkingLot lot = new ParkingLot("LOT1", "Downtown", 5);
        lot.setOccupiedSpaces(1);
        Vehicle vehicle = new Vehicle("ABC-123", VehicleType.CAR, "John Doe");
        vehicle.setParkingLot(lot);

        when(vehicleRepository.findById("ABC-123")).thenReturn(Optional.of(vehicle));
        when(parkingLotRepository.save(any())).thenReturn(lot);
        when(vehicleRepository.save(any())).thenReturn(vehicle);

        String result = parkingService.checkOutVehicle("ABC-123");

        assertEquals("Vehicle checked out successfully", result);  // updated to match service return
        assertNull(vehicle.getParkingLot());
        assertEquals(0, lot.getOccupiedSpaces());

        verify(vehicleRepository, times(1)).save(vehicle);
        verify(parkingLotRepository, times(1)).save(lot);
    }
}
