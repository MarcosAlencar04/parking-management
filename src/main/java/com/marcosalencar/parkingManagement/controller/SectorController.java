package com.marcosalencar.parkingManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcosalencar.parkingManagement.dto.GarageResponseDTO;
import com.marcosalencar.parkingManagement.service.SectorService;


@RestController
@CrossOrigin(origins = "*")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @GetMapping("/garage")
    public GarageResponseDTO getGarageInfo() {
        GarageResponseDTO garageInfo = sectorService.getGarageInfo();
        return garageInfo;
    }
    
}
