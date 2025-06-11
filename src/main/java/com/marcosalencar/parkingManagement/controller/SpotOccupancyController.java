package com.marcosalencar.parkingManagement.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marcosalencar.parkingManagement.dto.PlateStatusRequestDTO;
import com.marcosalencar.parkingManagement.dto.PlateStatusResponseDTO;
import com.marcosalencar.parkingManagement.dto.SpotOccupancyRequestDTO;
import com.marcosalencar.parkingManagement.service.SpotOccupancyService;



@RestController
@CrossOrigin(origins = "*")
public class SpotOccupancyController {

    @Autowired
    private SpotOccupancyService spotOccupancyService;

    @PostMapping("/webhook")
    public ResponseEntity<?> fluxControl(@RequestBody SpotOccupancyRequestDTO dto) {

        try {
            String message = spotOccupancyService.fluxControl(dto);
            return ResponseEntity.ok().body(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "Erro ao controlar fluxo do estacionamento",
                "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/plate-status")
    public ResponseEntity<?> plateStatus(@RequestBody PlateStatusRequestDTO dto) {
        try {
            PlateStatusResponseDTO response = spotOccupancyService.plateStatus(dto.license_plate());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "Erro ao buscar informações da placa",
                "error", e.getMessage()
            ));
        }
    }
    
}
