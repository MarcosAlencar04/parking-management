package com.marcosalencar.parkingManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcosalencar.parkingManagement.dto.GarageResponseDTO;
import com.marcosalencar.parkingManagement.dto.RevenueRequestDTO;
import com.marcosalencar.parkingManagement.dto.RevenueResponseDTO;
import com.marcosalencar.parkingManagement.service.SectorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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

    @PostMapping("/revenue")
    public RevenueResponseDTO revenue(@RequestBody RevenueRequestDTO dto) {
        RevenueResponseDTO response = sectorService.getRevenue(dto);
        return response;
    }
    

    
}
