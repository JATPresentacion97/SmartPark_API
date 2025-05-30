package com.SmartPark.SmartPark_API.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "VEHICLE")
public class Vehicle {

    @Id
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "License Plate can only contain letters, numbers and dashes")
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Owner name can contain only letters and spaces")
    private String ownerName;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    @JsonBackReference  // Prevent infinite recursion during JSON serialization
    private ParkingLot parkingLot;

    // Constructors
    public Vehicle() {
    }

    public Vehicle(String licensePlate, VehicleType type, String ownerName) {
        this.licensePlate = licensePlate;
        this.type = type;
        this.ownerName = ownerName;
    }

    // Getters and Setters

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
}
