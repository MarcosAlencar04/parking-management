package com.marcosalencar.parkingManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/spot-status")
    public SpotStatusResponseDTO spotStatus(@RequestBody SpotStatusRequestDTO dto) {
        SpotStatusResponseDTO response = spotService.spotStatus(dto.lat(), dto.lng());
        return response;
    }
    

}
