package com.marcosalencar.parkingManagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcosalencar.parkingManagement.dto.GarageResponseDTO;
import com.marcosalencar.parkingManagement.dto.RevenueRequestDTO;
import com.marcosalencar.parkingManagement.dto.RevenueResponseDTO;
import com.marcosalencar.parkingManagement.entity.Sector;
import com.marcosalencar.parkingManagement.entity.Spot;
import com.marcosalencar.parkingManagement.entity.SpotOccupancy;
import com.marcosalencar.parkingManagement.repository.SectorRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SectorService {

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private SpotService spotService;

    @Autowired
    private SpotOccupancyService spotOccupancyService;

    public GarageResponseDTO getGarageInfo(){
        List<Sector> sectors = sectorRepository.findAll();
        List<Spot> spots = spotService.getAllSpots();
        
        return GarageResponseDTO.fromEntities(sectors, spots);
    }

    public RevenueResponseDTO getRevenue(RevenueRequestDTO dto){
        LocalDateTime start = dto.date().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        try{
            List<SpotOccupancy> occupancies = spotOccupancyService.getBySectorAndDateRange(dto.sector(), start, end);
            String currency = occupancies.get(0).getSpot().getSector().getCurrency();
            double amount = occupancies.stream().mapToDouble(SpotOccupancy::getFinalPrice).sum();
            return new RevenueResponseDTO(amount, currency, start);
        } catch(Exception e){
            throw new EntityNotFoundException("Erro ao buscar veículos no setor " + dto.sector() + " estacionados no dia " + dto.date());
        }

    }
}
