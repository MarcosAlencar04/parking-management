package com.marcosalencar.parkingManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marcosalencar.parkingManagement.dto.DefaultResponseDTO;
import com.marcosalencar.parkingManagement.dto.ErrorResponseDTO;
import com.marcosalencar.parkingManagement.dto.SpotStatusRequestDTO;
import com.marcosalencar.parkingManagement.dto.SpotStatusResponseDTO;
import com.marcosalencar.parkingManagement.service.SpotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@CrossOrigin(origins = "*")
public class SpotController {

    @Autowired
    private SpotService spotService;

    @Operation(description = "Busca os dados de uma vaga pelas suas coordenadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Se estiver ocupada mostra a placa do carro, horário de entrada, preço até o momento e há quanto tempo está estacionado, se não tiver ocupada mostra valores padrões"),
        @ApiResponse(responseCode =  "404", description = "Nenhuma vaga cadastrada com as coordenadas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultResponseDTO.class))),
        @ApiResponse(responseCode =  "500", description = "Erro interno no servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/spot-status")
    public SpotStatusResponseDTO spotStatus(@RequestBody SpotStatusRequestDTO dto) {
        SpotStatusResponseDTO response = spotService.spotStatus(dto.lat(), dto.lng());
        return response;
    }
    

}
