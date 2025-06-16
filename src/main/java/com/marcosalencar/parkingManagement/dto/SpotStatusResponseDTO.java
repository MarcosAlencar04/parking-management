package com.marcosalencar.parkingManagement.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcosalencar.parkingManagement.entity.Spot;

public record SpotStatusResponseDTO(boolean occupied,
                                    @JsonProperty("license_plate") String license_plate,
                                    @JsonProperty("price_until_now" )double priceUntilNow,
                                    @JsonProperty("entry_time") LocalDateTime entryTime,
                                    @JsonProperty("time_parked")LocalDateTime timeParked) {

    public SpotStatusResponseDTO(Spot spot, String licensePlate, double priceUntilNow, LocalDateTime entryTime, LocalDateTime timeParked){   
        this(spot.isOccupied(),
             licensePlate,
             priceUntilNow,
             entryTime,
             timeParked);
    }

}
