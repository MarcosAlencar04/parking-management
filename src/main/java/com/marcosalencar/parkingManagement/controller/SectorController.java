package com.marcosalencar.parkingManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marcosalencar.parkingManagement.dto.DefaultResponseDTO;
import com.marcosalencar.parkingManagement.dto.ErrorResponseDTO;
import com.marcosalencar.parkingManagement.dto.GarageResponseDTO;
import com.marcosalencar.parkingManagement.dto.RevenueRequestDTO;
import com.marcosalencar.parkingManagement.dto.RevenueResponseDTO;
import com.marcosalencar.parkingManagement.service.SectorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;



@RestController
@CrossOrigin(origins = "*")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @Operation(description = "Busca os dados da garagem mostrando todos os setores e vagas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna um json com os dados da garagem"),
        @ApiResponse(responseCode =  "500", description = "Erro interno no servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/garage")
    public GarageResponseDTO getGarageInfo() {
        GarageResponseDTO garageInfo = sectorService.getGarageInfo();
        return garageInfo;
    }

    @Operation(description = "Busca o faturamento de um setor em um dia específico ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna um json com os dados do faturamento naquele dia"),
        @ApiResponse(responseCode =  "404", description = "Não há dados na garagem para o dia e/ou setor enviado na requisição",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultResponseDTO.class))),
        @ApiResponse(responseCode =  "500", description = "Erro interno no servidor",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/revenue")
    public RevenueResponseDTO revenue(@RequestBody RevenueRequestDTO dto) {
        RevenueResponseDTO response = sectorService.getRevenue(dto);
        return response;
    }
    

    
}
