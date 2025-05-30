package com.SmartPark.SmartPark_API.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.SmartPark.SmartPark_API.model.ParkingLot;
import com.SmartPark.SmartPark_API.model.Vehicle;
import com.SmartPark.SmartPark_API.model.VehicleType;
import com.SmartPark.SmartPark_API.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParkingController.class)
class ParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingService parkingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterParkingLot() throws Exception {
        ParkingLot lot = new ParkingLot("LOT1", "Downtown", 10);

        Mockito.when(parkingService.registerParkingLot(any(ParkingLot.class))).thenReturn(lot);

        mockMvc.perform(post("/api/parkinglots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lot)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lotId").value("LOT1"))
                .andExpect(jsonPath("$.location").value("Downtown"))
                .andExpect(jsonPath("$.capacity").value(10));
    }

    @Test
    void testRegisterVehicle() throws Exception {
        Vehicle vehicle = new Vehicle("ABC-123", VehicleType.CAR, "John Doe");

        Mockito.when(parkingService.registerVehicle(any(Vehicle.class))).thenReturn(vehicle);

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.licensePlate").value("ABC-123"))
                .andExpect(jsonPath("$.type").value("CAR"))
                .andExpect(jsonPath("$.ownerName").value("John Doe"));
    }

    @Test
    void testCheckInVehicle() throws Exception {
        Mockito.when(parkingService.checkInVehicle(eq("LOT1"), eq("ABC-123")))
                .thenReturn("Vehicle checked in successfully");

        mockMvc.perform(post("/api/parkinglots/LOT1/checkin/ABC-123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Vehicle checked in successfully"));
    }

    @Test
    void testCheckOutVehicle() throws Exception {
        Mockito.when(parkingService.checkOutVehicle(eq("ABC-123")))
                .thenReturn("Vehicle checked out successfully");

        mockMvc.perform(post("/api/vehicles/ABC-123/checkout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Vehicle checked out successfully"));
    }

    @Test
    void testGetParkingLotStatus() throws Exception {
        ParkingLot lot = new ParkingLot("LOT1", "Downtown", 10);
        lot.setOccupiedSpaces(3);

        Mockito.when(parkingService.getParkingLotInfo("LOT1")).thenReturn(Optional.of(lot));

        mockMvc.perform(get("/api/parkinglots/LOT1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lotId").value("LOT1"))
                .andExpect(jsonPath("$.location").value("Downtown"))
                .andExpect(jsonPath("$.capacity").value(10))
                .andExpect(jsonPath("$.occupiedSpaces").value(3));
    }
}
