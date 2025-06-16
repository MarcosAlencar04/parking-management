package com.marcosalencar.parkingManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcosalencar.parkingManagement.dto.PlateStatusResponseDTO;
import com.marcosalencar.parkingManagement.dto.SpotOccupancyRequestDTO;
import com.marcosalencar.parkingManagement.entity.Sector;
import com.marcosalencar.parkingManagement.entity.Spot;
import com.marcosalencar.parkingManagement.entity.SpotOccupancy;
import com.marcosalencar.parkingManagement.exception.BadRequestException;
import com.marcosalencar.parkingManagement.repository.SpotOccupancyRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SpotOccupancyService {

    @Autowired
    private SpotOccupancyRepository spotOccupancyRespository;

    @Autowired 
    private SpotService spotService;

    @Autowired
    private PricingService pricingService;

    private enum EventType {ENTRY, PARKED, EXIT};

    public List<SpotOccupancy> getBySectorAndDateRange(Character sector, LocalDateTime start, LocalDateTime end){
        return spotOccupancyRespository.findBySpot_Sector_SectorAndEntryTimeBetweenAndExitTimeNotNull(sector, start, end);
    }

    public SpotOccupancy getBySpot(Long idSpot){
        return spotOccupancyRespository.findBySpot_IdSpotAndExitTimeNull(idSpot);
    }

    private String manageEntry(SpotOccupancyRequestDTO dto) {
        Optional<SpotOccupancy> plateParked = spotOccupancyRespository.findByLicensePlateAndExitTimeNull(dto.licensePlate());

        if(plateParked.isPresent())
            throw new BadRequestException("Já existe um carro estacionado com a placa: " + plateParked.get().getLicensePlate());

        SpotOccupancy spotOccupancy = new SpotOccupancy();
        spotOccupancy.setLicensePlate(dto.licensePlate());
        spotOccupancy.setEntryTime(dto.entryTime());
        spotOccupancyRespository.save(spotOccupancy);
        return "Carro com a placa: " + spotOccupancy.getLicensePlate() + " entrou na garagem";
    }

    private String manageParked(SpotOccupancyRequestDTO dto) {
        SpotOccupancy lastOccupancy = spotOccupancyRespository.findTopByLicensePlateAndExitTimeIsNullOrderByIdOccupancyDesc(
                                                            dto.licensePlate()).orElseThrow(() ->
                                                            new EntityNotFoundException("Veículo da placa " + dto.licensePlate() + " não entrou no estacionamento")
                                                            );

        Spot spot = spotService.getSpotByLatAndLng(dto.lat(), dto.lng());
        Sector sector = spot.getSector();
        
        if(!sector.isOpen())
            throw new BadRequestException("Setor " + sector.getSector() + " está fechado");
        if(sector.isFull())
            throw new BadRequestException("Setor " + sector.getSector() + " está lotado");
        if(spot.isOccupied())
            throw new BadRequestException("Vaga já está ocupada");

        double pricePerHour = pricingService.calcPricePerHour(sector);
        lastOccupancy.setPricePerHour(pricePerHour);
        sector.setCurrentOccupancy(sector.getCurrentOccupancy()+1);
        spot.setSector(sector);
        spot.setOccupied(true);
        lastOccupancy.setSpot(spot);
        spotOccupancyRespository.save(lastOccupancy);
        return "Veículo estacionado com sucesso";
    }

    private String manageExit(SpotOccupancyRequestDTO dto) {
        SpotOccupancy lastOccupancy = spotOccupancyRespository.findTopByLicensePlateAndExitTimeIsNullOrderByIdOccupancyDesc(
                                                            dto.licensePlate()).orElseThrow(() ->
                                                            new EntityNotFoundException("Veículo da placa " + dto.licensePlate() + " não entrou no estacionamento")
                                                            );
        
        Spot spot = lastOccupancy.getSpot();
        Sector sector = spot.getSector();

        lastOccupancy.setExitTime(dto.exitTime());
        double finalPrice = pricingService.calcCurrentPrice(lastOccupancy.getPricePerHour(), lastOccupancy.getEntryTime(), lastOccupancy.getExitTime());
        lastOccupancy.setFinalPrice(finalPrice);
        sector.setCurrentOccupancy(sector.getCurrentOccupancy()-1);
        spot.setSector(sector);
        spot.setOccupied(false);
        lastOccupancy.setSpot(spot);
        spotOccupancyRespository.save(lastOccupancy);
        return "Veículo saiu do estacionamento, valor total: " + finalPrice;
    }

    public String fluxControl(SpotOccupancyRequestDTO dto) {
        String event = dto.eventType();
        if(event.equals(EventType.ENTRY.toString())){
            return manageEntry(dto);
        } else if(event.equals(EventType.PARKED.toString())) {
            return manageParked(dto);
        } else if (event.equals(EventType.EXIT.toString())){
            return manageExit(dto);
        } else{
            throw new BadRequestException("Tipo de evento inválido");
        }
    }

    public PlateStatusResponseDTO plateStatus(String licensePlate) {
        SpotOccupancy spotOccupancy = spotOccupancyRespository.findByLicensePlateAndExitTimeNull(licensePlate).orElseThrow(
                                                               () -> new EntityNotFoundException("Carro com a placa " + licensePlate + " não se encontra na garagem"));
        
        if(spotOccupancy.getSpot() == null)
            throw new EntityNotFoundException("Carro com a placa " + licensePlate + " não está estacionado");

        Double priceUntilNow = pricingService.calcCurrentPrice(spotOccupancy.getPricePerHour(), spotOccupancy.getEntryTime(), LocalDateTime.now());
        LocalDateTime timeParked = spotOccupancy.getTimeParked();
        PlateStatusResponseDTO response = new PlateStatusResponseDTO(spotOccupancy, priceUntilNow, timeParked);
        return response;
    }

}
