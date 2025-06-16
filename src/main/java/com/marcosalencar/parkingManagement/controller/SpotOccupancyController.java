package com.marcosalencar.parkingManagement.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> fluxControl(@RequestBody SpotOccupancyRequestDTO dto) {
        String message = spotOccupancyService.fluxControl(dto);
        return Map.of("message", message);
    }

    @PostMapping("/plate-status")
    public PlateStatusResponseDTO plateStatus(@RequestBody PlateStatusRequestDTO dto) {
        PlateStatusResponseDTO response = spotOccupancyService.plateStatus(dto.licensePlate());
        return response;
    }
    
}
