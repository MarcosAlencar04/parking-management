package com.marcosalencar.parkingManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marcosalencar.parkingManagement.entity.Sector;

@ExtendWith(MockitoExtension.class)
public class PricingServiceTest {
    @InjectMocks
    private PricingService pricingService;

    @Test
    void calcFinalPrice() {
        double pricePerHour = 10.0;
        LocalDateTime entryTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0, 0);
        LocalDateTime exitTime = LocalDateTime.of(2025, 1, 1, 14, 0, 0, 0);

        double expectedPrice = 20.0;

        double actualPrice = pricingService.calcCurrentPrice(pricePerHour, entryTime, exitTime);
        assertEquals(expectedPrice, actualPrice, 0.01);
    }

    @Test
    void calcPricePerHourLowOccupancy() throws Exception {
        Sector sectorLowOccupancy = new Sector();
        sectorLowOccupancy.setBasePrice(100.0);
        sectorLowOccupancy.setCurrentOccupancy(20);
        sectorLowOccupancy.setMaxCapacity(100);
    
        double expectedPriceLowOccupancy = 90.0;
    
        double actualPriceLowOccupancy = pricingService.calcPricePerHour(sectorLowOccupancy);
        assertEquals(expectedPriceLowOccupancy, actualPriceLowOccupancy, 0.01);
    }

    @Test
    void calcPricePerHourMediumOccupancy() throws Exception {
        Sector sectorMediumOccupancy = new Sector();
        sectorMediumOccupancy.setBasePrice(100.0);
        sectorMediumOccupancy.setCurrentOccupancy(50);
        sectorMediumOccupancy.setMaxCapacity(100);

        double expectedPriceMediumOccupancy = 100.0;

        double actualPriceMediumOccupancy = pricingService.calcPricePerHour(sectorMediumOccupancy);
        assertEquals(expectedPriceMediumOccupancy, actualPriceMediumOccupancy, 0.01);
    }

    @Test
    void calcPricePerHourHighOccupancy() throws Exception {
        Sector sectorHighOccupancy = new Sector();
        sectorHighOccupancy.setBasePrice(100.0);
        sectorHighOccupancy.setCurrentOccupancy(75);
        sectorHighOccupancy.setMaxCapacity(100);

        double expectedPriceHighOccupancy = 110.0;

        double actualPriceHighOccupancy = pricingService.calcPricePerHour(sectorHighOccupancy);
        assertEquals(expectedPriceHighOccupancy, actualPriceHighOccupancy, 0.01);
    }

    @Test
    void calcPricePerHourAlmostFullOccupancy() throws Exception {
        Sector sectorFullOccupancy = new Sector();
        sectorFullOccupancy.setBasePrice(100.0);
        sectorFullOccupancy.setCurrentOccupancy(99);
        sectorFullOccupancy.setMaxCapacity(100);

        double expectedPriceFullOccupancy = 125.0;

        double actualPriceFullOccupancy = pricingService.calcPricePerHour(sectorFullOccupancy);
        assertEquals(expectedPriceFullOccupancy, actualPriceFullOccupancy, 0.01);
    }
}
