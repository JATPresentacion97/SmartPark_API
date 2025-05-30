package com.SmartPark.SmartPark_API.controller;

import com.SmartPark.SmartPark_API.model.ParkingLot;
import com.SmartPark.SmartPark_API.model.Vehicle;
import com.SmartPark.SmartPark_API.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Validated
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/parkinglots")
    public ResponseEntity<ParkingLot> registerParkingLot(@Valid @RequestBody ParkingLot lot) {
        return new ResponseEntity<>(parkingService.registerParkingLot(lot), HttpStatus.CREATED);
    }

    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> registerVehicle(@Valid @RequestBody Vehicle vehicle) {
        return new ResponseEntity<>(parkingService.registerVehicle(vehicle), HttpStatus.CREATED);
    }

    @PostMapping("/parkinglots/{lotId}/checkin/{licensePlate}")
    public ResponseEntity<String> checkInVehicle(@PathVariable String lotId, @PathVariable String licensePlate) {
        String result = parkingService.checkInVehicle(lotId, licensePlate);
        if (result.equals("Vehicle checked in successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PostMapping("/vehicles/{licensePlate}/checkout")
    public ResponseEntity<String> checkOutVehicle(@PathVariable String licensePlate) {
        String result = parkingService.checkOutVehicle(licensePlate);
        if (result.equals("Vehicle checked out successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @GetMapping("/parkinglots/{lotId}")
    public ResponseEntity<ParkingLot> getParkingLotInfo(@PathVariable String lotId) {
        Optional<ParkingLot> lotOpt = parkingService.getParkingLotInfo(lotId);
        return lotOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/parkinglots/{lotId}/vehicles")
    public ResponseEntity<List<Vehicle>> getVehiclesInLot(@PathVariable String lotId) {
        List<Vehicle> vehicles = parkingService.getVehiclesInLot(lotId);
        if (vehicles == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicles);
    }
}
