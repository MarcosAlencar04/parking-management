package com.marcosalencar.parkingManagement.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.marcosalencar.parkingManagement.entity.Sector;

@Service
public class PricingService {
    private RangeMap<Double, Double> discountsMap;

    public PricingService() { 
        TreeRangeMap<Double, Double> map = TreeRangeMap.create();
        map.put(Range.closedOpen(0.0, 25.0),  0.90);
        map.put(Range.closed(25.0, 50.0), 1.00);
        map.put(Range.openClosed(50.0, 75.0), 1.10);
        map.put(Range.openClosed(75.0, 100.0), 1.25);
        this.discountsMap = ImmutableRangeMap.copyOf(map);
    }

    public double calcPricePerHour(Sector sector) {
        double pct = 100 * ((double)sector.getCurrentOccupancy()/sector.getMaxCapacity());
        Double discount = discountsMap.get(pct);

        if(discount == null)
            throw new RuntimeException("Ocupação do estacionamento fora da faixa comum");
        
        BigDecimal roundedValue = BigDecimal.valueOf(sector.getBasePrice() * discount)
                                            .setScale(2, RoundingMode.HALF_UP);
        return roundedValue.doubleValue();
    }

    public double calcCurrentPrice(Double pricePerHour, LocalDateTime entryTime, LocalDateTime referenceTime) {
        Duration duration = Duration.between(entryTime, referenceTime);
        long seconds = duration.getSeconds();
        double decimalHour = (double) seconds/3600;
        BigDecimal roundedValue = BigDecimal.valueOf(decimalHour * pricePerHour)
                                            .setScale(2, RoundingMode.HALF_UP);
        return roundedValue.doubleValue();
    }
}
