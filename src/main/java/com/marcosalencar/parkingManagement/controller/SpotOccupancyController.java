package com.marcosalencar.parkingManagement.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.marcosalencar.parkingManagement.dto.ErrorResponseDTO;
import com.marcosalencar.parkingManagement.dto.PlateStatusRequestDTO;
import com.marcosalencar.parkingManagement.dto.PlateStatusResponseDTO;
import com.marcosalencar.parkingManagement.dto.SpotOccupancyRequestDTO;
import com.marcosalencar.parkingManagement.dto.DefaultResponseDTO;
import com.marcosalencar.parkingManagement.service.SpotOccupancyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
public class SpotOccupancyController {

    @Autowired
    private SpotOccupancyService spotOccupancyService;

    @Operation(description = "Gerencia entrada e saída de veículos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Para ENTRY autoriza a entrada na garagem, para PARKED autoriza estacionar na vaga e para EXIT autoriza a saída da garagem",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultResponseDTO.class))),
        @ApiResponse(responseCode =  "400", description = "Requisição com algum 'eventType' não autorizado ou carro já estacionado com a placa enviada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultResponseDTO.class))),
        @ApiResponse(responseCode =  "404", description = "Veículo com a placa enviada não entrou no estacionamento",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultResponseDTO.class))),
        @ApiResponse(responseCode =  "500", description = "Erro interno no servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> fluxControl(@RequestBody SpotOccupancyRequestDTO dto) {
        String message = spotOccupancyService.fluxControl(dto);
        return Map.of("message", message);
    }

    @Operation(description = "Busca os dados de um carro pela sua placa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna a vaga que o carro ocupa, o preço até o momento, horário de entrada e quanto tempo está estacionado"),
        @ApiResponse(responseCode =  "404", description = "Carro não está na garagem ou não está estacionado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultResponseDTO.class))),
        @ApiResponse(responseCode =  "500", description = "Erro interno no servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/plate-status")
    public PlateStatusResponseDTO plateStatus(@RequestBody PlateStatusRequestDTO dto) {
        PlateStatusResponseDTO response = spotOccupancyService.plateStatus(dto.licensePlate());
        return response;
    }
    
}
