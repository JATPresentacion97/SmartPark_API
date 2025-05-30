package com.SmartPark.SmartPark_API.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PARKING_LOT")
public class ParkingLot {

    @Id
    @Size(max = 50)
    private String lotId;

    private String location;

    private int capacity;

    private int occupiedSpaces;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // Add this annotation to manage JSON serialization
    private List<Vehicle> vehicles = new ArrayList<>();

    // Constructors
    public ParkingLot() {
    }

    public ParkingLot(String lotId, String location, int capacity) {
        this.lotId = lotId;
        this.location = location;
        this.capacity = capacity;
        this.occupiedSpaces = 0;
    }

    // Getters and Setters

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupiedSpaces() {
        return occupiedSpaces;
    }

    public void setOccupiedSpaces(int occupiedSpaces) {
        this.occupiedSpaces = occupiedSpaces;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    // Helper methods
    public boolean isFull() {
        return occupiedSpaces >= capacity;
    }

    public void incrementOccupied() {
        this.occupiedSpaces++;
    }

    public void decrementOccupied() {
        if (this.occupiedSpaces > 0) {
            this.occupiedSpaces--;
        }
    }
}
