package com.marcosalencar.parkingManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marcosalencar.parkingManagement.dto.RevenueRequestDTO;
import com.marcosalencar.parkingManagement.dto.RevenueResponseDTO;
import com.marcosalencar.parkingManagement.entity.Sector;
import com.marcosalencar.parkingManagement.entity.Spot;
import com.marcosalencar.parkingManagement.entity.SpotOccupancy;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class SectorServiceTest {

    @InjectMocks
    private SectorService sectorService;
    
    @Mock
    private SpotOccupancyService spotOccupancyService;

    private RevenueRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new RevenueRequestDTO(LocalDate.of(2025, 6, 13), 'A');
    }

    @Test
    void testGetRevenueSuccess() {
        SpotOccupancy occ1 = new SpotOccupancy();
        Spot s1 = new Spot();
        Sector sector1 = new Sector();
        sector1.setCurrency("BRL");
        s1.setSector(sector1);
        occ1.setSpot(s1);
        occ1.setFinalPrice(120.50);

        SpotOccupancy occ2 = new SpotOccupancy();
        Spot s2 = new Spot();
        Sector sector2 = new Sector();
        sector2.setCurrency("BRL");
        s2.setSector(sector2);
        occ2.setSpot(s2);
        occ2.setFinalPrice(79.50);

        LocalDateTime expectedStart = validRequest.date().atStartOfDay();
        LocalDateTime expectedEnd   = expectedStart.plusDays(1);

        when(spotOccupancyService.getBySectorAndDateRange(
                eq(validRequest.sector()),
                eq(expectedStart),
                eq(expectedEnd)))
            .thenReturn(List.of(occ1, occ2));

        RevenueResponseDTO response = sectorService.getRevenue(validRequest);

        assertEquals(200.0, response.amount(), 0.001);
        assertEquals("BRL", response.currency());
        assertEquals(expectedStart, response.timestamp());
    }

    @Test
    void testGetRevenueNoOccupancies() {
        when(spotOccupancyService.getBySectorAndDateRange(
                anyChar(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(null);

        EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> sectorService.getRevenue(validRequest)
        );
        assertEquals(
            "Erro ao buscar veículos no setor " + validRequest.sector() + " estacionados no dia " + validRequest.date(),
            ex.getMessage()
        );
    }
}