package com.marcosalencar.parkingManagement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.marcosalencar.parkingManagement.entity.SpotOccupancy;

public record PlateStatusResponseDTO(String license_plate,
                                     Double price_until_now,
                                     LocalDateTime entry_time,
                                     LocalDateTime time_parked,
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
