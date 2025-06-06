package com.marcosalencar.parkingManagement.dto;

import java.math.BigDecimal;

import com.marcosalencar.parkingManagement.entity.Spot;

public record SpotResponseDTO(Long id,
                              Character sector,
                              BigDecimal lat,
                              BigDecimal lng,
                              Boolean occupied) {

    public SpotResponseDTO(Spot spot){
        this(spot.getIdSpot(),
             spot.getSector().getSector(),
             spot.getLat(),
             spot.getLng(),
             spot.getOccupied());
    }

}
