package com.marcosalencar.parkingManagement.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.marcosalencar.parkingManagement.dto.SpotStatusResponseDTO;
import com.marcosalencar.parkingManagement.entity.Spot;
import com.marcosalencar.parkingManagement.entity.SpotOccupancy;
import com.marcosalencar.parkingManagement.repository.SpotRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SpotService {

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    @Lazy
    private SpotOccupancyService spotOccupancyService;
    
    @Autowired
    private PricingService pricingService;

    public List<Spot> getAllSpots(){
        return spotRepository.findAll();
    }

    public Spot getSpotByLatAndLng(BigDecimal lat, BigDecimal lng) {
        return spotRepository.findByLatAndLng(lat, lng);
    }

    public SpotStatusResponseDTO spotStatus(BigDecimal lat, BigDecimal lng){
        Spot spot = getSpotByLatAndLng(lat, lng);
        if(spot == null)
            throw new EntityNotFoundException("Nenhuma vaga cadastrada com as coordenadas: lat = " + lat + " lng = " + lng);

        if(spot.isOccupied()){
            SpotOccupancy occupancy = spotOccupancyService.getBySpot(spot.getIdSpot());
            LocalDateTime entryTime = occupancy.getEntryTime();
            double priceUntilNow = pricingService.calcCurrentPrice(occupancy.getPricePerHour(), entryTime, LocalDateTime.now());
            LocalDateTime timeParked = occupancy.getTimeParked();
            SpotStatusResponseDTO response = new SpotStatusResponseDTO(spot.isOccupied(), 
                                                                       occupancy.getLicensePlate(),
                                                                       priceUntilNow, 
                                                                       entryTime, 
                                                                       timeParked);
            return response;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);
        
        SpotStatusResponseDTO response = new SpotStatusResponseDTO(spot.isOccupied(),
                                                                   "",
                                                                   0,
                                                                   LocalDateTime.parse(formattedTime, formatter),
                                                                   LocalDateTime.parse(formattedTime, formatter));
        return response;
        
    }
}
