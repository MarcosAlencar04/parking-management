package com.marcosalencar.parkingManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marcosalencar.parkingManagement.dto.PlateStatusResponseDTO;
import com.marcosalencar.parkingManagement.dto.SpotOccupancyRequestDTO;
import com.marcosalencar.parkingManagement.entity.Sector;
import com.marcosalencar.parkingManagement.entity.Spot;
import com.marcosalencar.parkingManagement.entity.SpotOccupancy;
import com.marcosalencar.parkingManagement.repository.SpotOccupancyRepository;

@ExtendWith(MockitoExtension.class)
public class SpotOccupancyServiceTest {

    @InjectMocks
    private SpotOccupancyService spotOccupancyService;

    @Mock
    private SpotOccupancyRepository spotOccupancyRespository;

    @Mock
    private SpotService spotService;

    @Mock
    private SectorService sectorService;

    private SpotOccupancyRequestDTO validEntryDTO;
    private SpotOccupancyRequestDTO validParkedDTO;
    private SpotOccupancyRequestDTO validExitDTO;

    @BeforeEach
    void setUp() {
        validEntryDTO = new SpotOccupancyRequestDTO("ZUL0001", LocalDateTime.of(2025, 1, 1, 12, 0, 0, 0), "ENTRY", null, null, null);
        validParkedDTO = new SpotOccupancyRequestDTO("ZUL0001", null, "PARKED", new BigDecimal("-23.561684"), new BigDecimal("-46.655981"), null);
        validExitDTO = new SpotOccupancyRequestDTO("ZUL0001", null, "EXIT", null, null, LocalDateTime.of(2025, 1, 1, 14, 0, 0, 0));
    }

    @Test
    void calcFinalPrice() {
        double pricePerHour = 10.0;
        LocalDateTime entryTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2025, 1, 1, 14, 30, 0, 0);  // 2 horas de permanência

        double expectedPrice = 25.0;

        double actualPrice = spotOccupancyService.calcCurrentPrice(pricePerHour, entryTime, exitTime);
        assertEquals(expectedPrice, actualPrice, 0.01);
    }

    @Test
    void fluxControlEntry() throws Exception {
        when(spotOccupancyRespository.findByLicensePlateAndExitTimeNull(any(String.class))).thenReturn(Optional.empty());
        when(spotOccupancyRespository.save(any(SpotOccupancy.class))).thenReturn(new SpotOccupancy());

        String response = spotOccupancyService.fluxControl(validEntryDTO);
        assertEquals("Carro com a placa: ZUL0001 entrou na garagem", response);
    }

    @Test
    void fluxControlEntryWithCarAlreadyParked() throws Exception {
        SpotOccupancy existingSpotOccupancy = new SpotOccupancy();
        existingSpotOccupancy.setLicensePlate("ZUL0001");
        existingSpotOccupancy.setExitTime(null);
        
        when(spotOccupancyRespository.findByLicensePlateAndExitTimeNull("ZUL0001")).thenReturn(Optional.of(existingSpotOccupancy));

        Exception exception = assertThrows(Exception.class, () -> spotOccupancyService.fluxControl(validEntryDTO));
        assertEquals("Já existe um carro estacionado com a placa: ZUL0001", exception.getMessage());
    }

    @Test
    void fluxControlParked() throws Exception {
        SpotOccupancy spotOccupancy = new SpotOccupancy();
        spotOccupancy.setLicensePlate("ZUL0001");
        spotOccupancy.setExitTime(null);

        Spot spot = new Spot();
        Sector sector = new Sector();
        sector.setSector('A');
        sector.setMaxCapacity(10);
        sector.setCurrentOccupancy(5);

        spot.setSector(sector);
        spot.setOccupied(false);

        when(spotOccupancyRespository.findTopByLicensePlateOrderByIdOccupancyDesc("ZUL0001")).thenReturn(Optional.of(spotOccupancy));
        when(spotService.getSpotByLatAndLng(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(spot);
        when(sectorService.isSectorOpen(sector)).thenReturn(true);
        when(sectorService.isSectorFull(sector)).thenReturn(false);
        when(sectorService.calcPricePerHour(sector)).thenReturn(10.0);

        String response = spotOccupancyService.fluxControl(validParkedDTO);
        assertEquals("Veículo estacionado com sucesso", response);
    }

    @Test
    void fluxControlParkedSpotOccupied() throws Exception {
        SpotOccupancy spotOccupancy = new SpotOccupancy();
        spotOccupancy.setLicensePlate("ZUL0001");
        spotOccupancy.setExitTime(null);
    
        Spot spot = new Spot();
        Sector sector = new Sector();
        sector.setSector('A');
        sector.setMaxCapacity(10);
        sector.setCurrentOccupancy(5);
        sector.setOpenHour(LocalTime.of(0, 0));
        sector.setCloseHour(LocalTime.of(23, 59));
    
        spot.setSector(sector);
        spot.setOccupied(true);
    
        when(sectorService.isSectorOpen(sector)).thenReturn(true);
    
        when(spotOccupancyRespository.findTopByLicensePlateOrderByIdOccupancyDesc("ZUL0001")).thenReturn(Optional.of(spotOccupancy));
        when(spotService.getSpotByLatAndLng(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(spot);
    
        Exception exception = assertThrows(Exception.class, () -> spotOccupancyService.fluxControl(validParkedDTO));
        assertEquals("Vaga já está ocupada", exception.getMessage());
    }
    
    @Test
    void fluxControlParkedWithVehicleNotEntered() throws Exception {
        SpotOccupancy spotOccupancy = new SpotOccupancy();
        spotOccupancy.setLicensePlate("ZUL0001");
        spotOccupancy.setExitTime(null);
    
        Spot spot = new Spot();
        Sector sector = new Sector();
        sector.setSector('A');
        sector.setMaxCapacity(10);
        sector.setCurrentOccupancy(5);
    
        spot.setSector(sector);
        spot.setOccupied(false);

        when(sectorService.isSectorOpen(sector)).thenReturn(true);
        when(spotOccupancyRespository.findTopByLicensePlateOrderByIdOccupancyDesc("ZUL0001"))
            .thenReturn(Optional.of(spotOccupancy));
        when(spotService.getSpotByLatAndLng(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(spot);
    
        spotOccupancy.setExitTime(LocalDateTime.now());
        Exception exception = assertThrows(Exception.class, () -> spotOccupancyService.fluxControl(validParkedDTO));
        assertEquals("Veículo não entrou no estacionamento", exception.getMessage());
    }
    
    @Test
    void fluxControlExit() throws Exception {
        SpotOccupancy spotOccupancy = new SpotOccupancy();
        spotOccupancy.setLicensePlate("ZUL0001");
        spotOccupancy.setEntryTime(LocalDateTime.of(2025, 1, 1, 12, 0, 0, 0));
        spotOccupancy.setExitTime(null);
    
        Spot spot = new Spot();
        Sector sector = new Sector();
        sector.setSector('A');
        sector.setMaxCapacity(10);
        sector.setCurrentOccupancy(5);
    
        spot.setSector(sector);
        spotOccupancy.setSpot(spot);
        spotOccupancy.setPricePerHour(15.0);
    
        when(spotOccupancyRespository.findTopByLicensePlateOrderByIdOccupancyDesc("ZUL0001")).thenReturn(Optional.of(spotOccupancy));
    
        String response = spotOccupancyService.fluxControl(validExitDTO);
        assertEquals("Baixa dada do veículo no estacionamento", response);
    }
 
    @Test
    void testPlateStatusSuccess() throws Exception {
        String licensePlate = "ZUL0001";

        SpotOccupancy spotOccupancy = new SpotOccupancy();
        spotOccupancy.setLicensePlate(licensePlate);
        spotOccupancy.setEntryTime(LocalDateTime.of(2025, 1, 1, 12, 0, 0, 0));
        spotOccupancy.setExitTime(null);
        spotOccupancy.setPricePerHour(10.0);
        
        Spot spot = new Spot();
        Sector sector = new Sector();
        sector.setSector('A');
        sector.setMaxCapacity(10);
        sector.setCurrentOccupancy(5);
    
        spot.setSector(sector);
        spotOccupancy.setSpot(spot);

        when(spotOccupancyRespository.findByLicensePlateAndExitTimeNull(licensePlate))
                .thenReturn(Optional.of(spotOccupancy));

        double expectedPrice = spotOccupancyService.calcCurrentPrice(10.0, LocalDateTime.of(2025, 1, 1, 12, 0, 0, 0), LocalDateTime.now());

        PlateStatusResponseDTO response = spotOccupancyService.plateStatus(licensePlate);

        assertEquals(licensePlate, response.license_plate());
        assertEquals(expectedPrice, response.price_until_now(), 0.01);
    }

    @Test
    void testPlateStatusCarNotFound() {
        String licensePlate = "ZUL0001";

        when(spotOccupancyRespository.findByLicensePlateAndExitTimeNull(licensePlate))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> spotOccupancyService.plateStatus(licensePlate));
        assertEquals("Carro com a placa ZUL0001 não se encontra estacionado no momento", exception.getMessage());
    }

}
