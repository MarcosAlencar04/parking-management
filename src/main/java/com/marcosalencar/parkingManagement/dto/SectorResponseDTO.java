package com.marcosalencar.parkingManagement.dto;

import java.time.LocalTime;

import com.marcosalencar.parkingManagement.entity.Sector;

public record SectorResponseDTO(Character sector,
                                Double basePrice,
                                Integer maxCapacity,
                                LocalTime openHour,
                                LocalTime closeHour,
                                Integer durationLimitMinutes) {

    
    public SectorResponseDTO(Sector sector){
        this(sector.getSector(),
             sector.getBasePrice(),
             sector.getMaxCapacity(),
             sector.getOpenHour(),
             sector.getCloseHour(),
             sector.getDurationLimitMinutes());
    }
}
