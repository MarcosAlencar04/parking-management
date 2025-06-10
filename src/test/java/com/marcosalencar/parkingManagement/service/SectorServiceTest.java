package com.marcosalencar.parkingManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marcosalencar.parkingManagement.entity.Sector;
import com.marcosalencar.parkingManagement.repository.SectorRepository;

@ExtendWith(MockitoExtension.class)
public class SectorServiceTest {

    @InjectMocks
    private SectorService sectorService;

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private SpotService spotService;

    @Test
    void isSectorOpen() {
        Sector sectorOpen = new Sector();
        sectorOpen.setOpenHour(LocalTime.of(00, 0));
        sectorOpen.setCloseHour(LocalTime.of(23, 59)); 
    
        boolean isOpen = sectorService.isSectorOpen(sectorOpen);
    
        assertTrue(isOpen);
    }
    
    @Test
    void isSectorClosed() {
        Sector sectorClosed = new Sector();
        sectorClosed.setOpenHour(LocalTime.of(8, 0)); 
        sectorClosed.setCloseHour(LocalTime.of(8, 1));

        boolean isClosed = sectorService.isSectorOpen(sectorClosed);
    
        assertFalse(isClosed);
    }
    
    @Test
    void calcPricePerHourLowOccupancy() throws Exception {
        SectorService sectorService = new SectorService();
        sectorService.initDiscountsMap();

        Sector sectorLowOccupancy = new Sector();
        sectorLowOccupancy.setBasePrice(100.0);
        sectorLowOccupancy.setCurrentOccupancy(20);
        sectorLowOccupancy.setMaxCapacity(100);
    
        double expectedPriceLowOccupancy = 90.0;
    
        double actualPriceLowOccupancy = sectorService.calcPricePerHour(sectorLowOccupancy);
        assertEquals(expectedPriceLowOccupancy, actualPriceLowOccupancy, 0.01);
    }

    @Test
    void calcPricePerHourMediumOccupancy() throws Exception {
        SectorService sectorService = new SectorService();
        sectorService.initDiscountsMap();
        
        Sector sectorMediumOccupancy = new Sector();
        sectorMediumOccupancy.setBasePrice(100.0);
        sectorMediumOccupancy.setCurrentOccupancy(50);
        sectorMediumOccupancy.setMaxCapacity(100);

        double expectedPriceMediumOccupancy = 100.0;

        double actualPriceMediumOccupancy = sectorService.calcPricePerHour(sectorMediumOccupancy);
        assertEquals(expectedPriceMediumOccupancy, actualPriceMediumOccupancy, 0.01);
    }

    @Test
    void calcPricePerHourHighOccupancy() throws Exception {
        SectorService sectorService = new SectorService();
        sectorService.initDiscountsMap();
       
        Sector sectorHighOccupancy = new Sector();
        sectorHighOccupancy.setBasePrice(100.0);
        sectorHighOccupancy.setCurrentOccupancy(75);
        sectorHighOccupancy.setMaxCapacity(100);

        double expectedPriceHighOccupancy = 110.0;

        double actualPriceHighOccupancy = sectorService.calcPricePerHour(sectorHighOccupancy);
        assertEquals(expectedPriceHighOccupancy, actualPriceHighOccupancy, 0.01);
    }

    @Test
    void calcPricePerHourAlmostFullOccupancy() throws Exception {
        SectorService sectorService = new SectorService();
        sectorService.initDiscountsMap();
        
        Sector sectorFullOccupancy = new Sector();
        sectorFullOccupancy.setBasePrice(100.0);
        sectorFullOccupancy.setCurrentOccupancy(99);
        sectorFullOccupancy.setMaxCapacity(100);

        double expectedPriceFullOccupancy = 125.0;

        double actualPriceFullOccupancy = sectorService.calcPricePerHour(sectorFullOccupancy);
        assertEquals(expectedPriceFullOccupancy, actualPriceFullOccupancy, 0.01);
    }

}
