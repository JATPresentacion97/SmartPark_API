package com.SmartPark.SmartPark_API.service;

import com.SmartPark.SmartPark_API.model.ParkingLot;
import com.SmartPark.SmartPark_API.model.Vehicle;
import com.SmartPark.SmartPark_API.repository.ParkingLotRepository;
import com.SmartPark.SmartPark_API.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingService {

    private final ParkingLotRepository parkingLotRepository;
    private final VehicleRepository vehicleRepository;

    public ParkingService(ParkingLotRepository parkingLotRepository, VehicleRepository vehicleRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // Register a parking lot
    public ParkingLot registerParkingLot(ParkingLot lot) {
        lot.setOccupiedSpaces(0);
        return parkingLotRepository.save(lot);
    }

    // Register a vehicle with validation for duplicate license plate
    public Vehicle registerVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsById(vehicle.getLicensePlate())) {
            throw new IllegalArgumentException("Vehicle with license plate " + vehicle.getLicensePlate() + " already exists.");
        }
        return vehicleRepository.save(vehicle);
    }

    // Check-in vehicle to parking lot
    @Transactional
    public String checkInVehicle(String lotId, String licensePlate) {
        Optional<ParkingLot> lotOpt = parkingLotRepository.findById(lotId);
        if (!lotOpt.isPresent()) return "Parking lot not found";

        ParkingLot lot = lotOpt.get();

        if (lot.isFull()) return "Parking lot is full";

        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(licensePlate);
        if (!vehicleOpt.isPresent()) return "Vehicle not registered";

        Vehicle vehicle = vehicleOpt.get();

        // Vehicle can only be parked in one lot at a time
        if (vehicle.getParkingLot() != null) {
            return "Vehicle is already parked in a lot";
        }

        // Assign vehicle to parking lot
        vehicle.setParkingLot(lot);
        vehicleRepository.save(vehicle);

        // Update occupied spaces
        lot.incrementOccupied();
        parkingLotRepository.save(lot);

        return "Vehicle checked in successfully";
    }

    // Check-out vehicle from parking lot
    @Transactional
    public String checkOutVehicle(String licensePlate) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(licensePlate);
        if (!vehicleOpt.isPresent()) return "Vehicle not found";

        Vehicle vehicle = vehicleOpt.get();
        ParkingLot lot = vehicle.getParkingLot();

        if (lot == null) return "Vehicle is not parked in any lot";

        // Remove vehicle from lot
        vehicle.setParkingLot(null);
        vehicleRepository.save(vehicle);

        // Update occupied spaces
        lot.decrementOccupied();
        parkingLotRepository.save(lot);

        return "Vehicle checked out successfully";
    }

    // View current occupancy and availability of a parking lot
    public Optional<ParkingLot> getParkingLotInfo(String lotId) {
        return parkingLotRepository.findById(lotId);
    }

    // View all vehicles currently parked in a lot
    public List<Vehicle> getVehiclesInLot(String lotId) {
        Optional<ParkingLot> lotOpt = parkingLotRepository.findById(lotId);
        return lotOpt.map(ParkingLot::getVehicles).orElse(null);
    }
}
