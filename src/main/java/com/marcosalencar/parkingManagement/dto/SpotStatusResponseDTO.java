package com.marcosalencar.parkingManagement.dto;

import java.time.LocalDateTime;

import com.marcosalencar.parkingManagement.entity.Spot;

public record SpotStatusResponseDTO(boolean occupied,
                                    String license_plate,
                                    double price_until_now,
                                    LocalDateTime entry_time,
                                    LocalDateTime time_parked) {

    public SpotStatusResponseDTO(Spot spot, String licensePlate, double priceUntilNow, LocalDateTime entryTime, LocalDateTime timeParked){   
        this(spot.isOccupied(),
             licensePlate,
             priceUntilNow,
             entryTime,
             timeParked);
    }

}
