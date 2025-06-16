package com.marcosalencar.parkingManagement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcosalencar.parkingManagement.entity.SpotOccupancy;

public record PlateStatusResponseDTO(@JsonProperty("license_plate") String licensePlate,
                                     @JsonProperty("price_until_now") Double priceUntilNow,
                                     @JsonProperty("entry_time") LocalDateTime entryTime,
                                     @JsonProperty("time_parked") LocalDateTime timeParked,
                                     BigDecimal lat,
                                     BigDecimal lng) {
    
    public PlateStatusResponseDTO(SpotOccupancy spotOccupancy, Double priceUntilNow, LocalDateTime timeParked){
        this(spotOccupancy.getLicensePlate(),
             priceUntilNow,
             spotOccupancy.getEntryTime(),
             timeParked,
             spotOccupancy.getSpot().getLat(),
             spotOccupancy.getSpot().getLng());
    }
}
