package com.marcosalencar.parkingManagement.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.marcosalencar.parkingManagement.dto.GarageResponseDTO;
import com.marcosalencar.parkingManagement.entity.Sector;
import com.marcosalencar.parkingManagement.entity.Spot;
import com.marcosalencar.parkingManagement.repository.SectorRepository;

import jakarta.annotation.PostConstruct;

@Service
public class SectorService {

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private SpotService spotService;

    private RangeMap<Double, Double> discountsMap;

    @PostConstruct
    public void initDiscountsMap(){
        TreeRangeMap<Double, Double> map = TreeRangeMap.create();
        map.put(Range.closedOpen(0.0, 25.0),  0.90);
        map.put(Range.closed(25.0, 50.0), 1.00);
        map.put(Range.openClosed(50.0, 75.0), 1.10);
        map.put(Range.openClosed(75.0, 100.0), 1.25);
        this.discountsMap = ImmutableRangeMap.copyOf(map);
    }

    public GarageResponseDTO getGarageInfo(){
        List<Sector> sectors = sectorRepository.findAll();
        List<Spot> spots = spotService.getAllSpots();
        
        return GarageResponseDTO.fromEntities(sectors, spots);
    }

    public boolean isSectorOpen(Sector sector) {
        LocalTime openHour = sector.getOpenHour();
        LocalTime closeHour = sector.getCloseHour();
        LocalTime now = LocalTime.now();
        
        if(openHour.isBefore(closeHour)){
            return !now.isBefore(openHour) && now.isBefore(closeHour);
        }

        return !now.isBefore(openHour) || now.isBefore((closeHour));
    }

    public boolean isSectorFull(Sector sector){
        double occupancy = sector.getCurrentOccupancy()/sector.getMaxCapacity();
        return occupancy == 1 ? true : false;
    }

    public double calcPricePerHour(Sector sector) throws Exception{
        double pct = 100 * ((double)sector.getCurrentOccupancy()/sector.getMaxCapacity());
        Double discount = discountsMap.get(pct);

        if(discount == null)
            throw new Exception("Ocupação do estacionamento fora da faixa comum");
        
        BigDecimal roundedValue = BigDecimal.valueOf(sector.getBasePrice() * discount)
                                            .setScale(2, RoundingMode.HALF_UP);
        return roundedValue.doubleValue();
    }
}
