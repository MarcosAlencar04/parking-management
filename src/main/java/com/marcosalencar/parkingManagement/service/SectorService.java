package com.marcosalencar.parkingManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcosalencar.parkingManagement.dto.GarageResponseDTO;
import com.marcosalencar.parkingManagement.entity.Sector;
import com.marcosalencar.parkingManagement.entity.Spot;
import com.marcosalencar.parkingManagement.repository.SectorRepository;

@Service
public class SectorService {

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private SpotService spotService;

    public GarageResponseDTO getGarageInfo(){
        List<Sector> sectors = sectorRepository.findAll();
        List<Spot> spots = spotService.getAllSpots();
        
        return GarageResponseDTO.fromEntities(sectors, spots);
    }


}
