package com.marcosalencar.parkingManagement.dto;

import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcosalencar.parkingManagement.entity.Sector;

public record SectorResponseDTO(Character sector,
                                @JsonProperty("base_price") Double basePrice,
                                @JsonProperty("max_capacity") Integer maxCapacity,
                                @JsonProperty("open_hour") String openHour,
                                @JsonProperty("close_hour") String closeHour,
                                @JsonProperty("duration_limit_minutes") Integer durationLimitMinutes) {

    
    public SectorResponseDTO(Sector sector){
        this(sector.getSector(),
             sector.getBasePrice(),
             sector.getMaxCapacity(),
             sector.getOpenHour().format(DateTimeFormatter.ofPattern("HH:mm")),
             sector.getCloseHour().format(DateTimeFormatter.ofPattern("HH:mm")),
             sector.getDurationLimitMinutes());
    }
}
