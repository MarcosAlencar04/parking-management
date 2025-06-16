package com.marcosalencar.parkingManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marcosalencar.parkingManagement.dto.SpotStatusResponseDTO;
import com.marcosalencar.parkingManagement.entity.Spot;
import com.marcosalencar.parkingManagement.entity.SpotOccupancy;
import com.marcosalencar.parkingManagement.repository.SpotRepository;

@ExtendWith(MockitoExtension.class)
public class SpotServiceTest {

    @InjectMocks
    private SpotService spotService;

    @Mock
    private SpotOccupancyService spotOccupancyService;

    @Mock
    private SpotRepository spotRepository;

    @Mock
    private PricingService pricingService;

    private Spot spot;

    @BeforeEach
    void setUp() {
        spot = new Spot();
        spot.setIdSpot(1L);
        spot.setOccupied(true);
    }

    @Test
    void testSpotStatusWhenOccupied() {
        SpotOccupancy spotOccupancy = new SpotOccupancy();
        spotOccupancy.setLicensePlate("ZUL0001");
        spotOccupancy.setEntryTime(LocalDateTime.of(2025, 1, 1, 12, 0, 0, 0));
        spotOccupancy.setPricePerHour(10.0);

        when(spotRepository.findByLatAndLng(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(spot);
        when(spotOccupancyService.getBySpot(spot.getIdSpot())).thenReturn(spotOccupancy);

        lenient().when(pricingService.calcCurrentPrice(any(Double.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                 .thenReturn(20.0);

        SpotStatusResponseDTO response = spotService.spotStatus(BigDecimal.valueOf(-23.561684), BigDecimal.valueOf(-46.655981));

        assertEquals(true, response.occupied());
        assertEquals("ZUL0001", response.license_plate());
        assertEquals(20.0, response.priceUntilNow(), 0.01);
    }

    @Test
    void testSpotStatusWhenNotOccupied() {
        spot.setOccupied(false);

        when(spotRepository.findByLatAndLng(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(spot);

        SpotStatusResponseDTO response = spotService.spotStatus(new BigDecimal("-23.561684"), new BigDecimal("-46.655981"));

        assertEquals(false, response.occupied());
        assertEquals("", response.license_plate());
        assertEquals(0, response.priceUntilNow(), 0.01);
    }

}