package com.marcosalencar.parkingManagement.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marcosalencar.parkingManagement.dto.SpotStatusRequestDTO;
import com.marcosalencar.parkingManagement.dto.SpotStatusResponseDTO;
import com.marcosalencar.parkingManagement.service.SpotService;


@RestController
@CrossOrigin(origins = "*")
public class SpotController {

    @Autowired
    private SpotService spotService;

    @PostMapping("spot-status")
    public ResponseEntity<?> spotStatus(@RequestBody SpotStatusRequestDTO dto) {
        try {
            SpotStatusResponseDTO response = spotService.spotStatus(dto.lat(), dto.lng());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "Erro ao buscar informações da vaga",
                "error", e.getMessage()
            ));
        }
    }
    

}
