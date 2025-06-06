package com.marcosalencar.parkingManagement.dto;

import java.time.format.DateTimeFormatter;

import com.marcosalencar.parkingManagement.entity.Sector;

public record SectorResponseDTO(Character sector,
                                Double base_price,
                                Integer max_capacity,
                                String open_hour,
                                String close_hour,
                                Integer duration_limit_minutes) {

    
    public SectorResponseDTO(Sector sector){
        this(sector.getSector(),
             sector.getBasePrice(),
             sector.getMaxCapacity(),
             sector.getOpenHour().format(DateTimeFormatter.ofPattern("HH:mm")),
             sector.getCloseHour().format(DateTimeFormatter.ofPattern("HH:mm")),
             sector.getDurationLimitMinutes());
    }
}
