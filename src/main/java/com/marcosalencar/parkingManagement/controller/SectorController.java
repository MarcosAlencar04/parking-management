package com.marcosalencar.parkingManagement.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcosalencar.parkingManagement.dto.GarageResponseDTO;
import com.marcosalencar.parkingManagement.service.SectorService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/sector")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @GetMapping("/garage")
    public ResponseEntity<?> getGarageInfo() {
        try {
            GarageResponseDTO garageInfo = sectorService.getGarageInfo();

            return ResponseEntity.ok(garageInfo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "message", "Erro ao buscar informações da garagem",
                "error", e.getMessage()));
        }
    }
    
}
