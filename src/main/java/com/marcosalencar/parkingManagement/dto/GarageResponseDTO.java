package com.marcosalencar.parkingManagement.dto;

import java.util.List;

import com.marcosalencar.parkingManagement.entity.Sector;
import com.marcosalencar.parkingManagement.entity.Spot;

public record GarageResponseDTO(List<SectorResponseDTO> garage,
                                List<SpotResponseDTO> spots){

    public static GarageResponseDTO fromEntities(List<Sector> sectorsEntity, List<Spot> spotsEntity){
        
        return new GarageResponseDTO(sectorsEntity.stream().map(SectorResponseDTO::new).toList(),
                                     spotsEntity.stream().map(SpotResponseDTO::new).toList()
                                    );
    }
                                        
}
    
