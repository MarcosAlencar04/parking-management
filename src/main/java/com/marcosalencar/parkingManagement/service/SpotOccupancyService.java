package com.marcosalencar.parkingManagement.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcosalencar.parkingManagement.dto.PlateStatusResponseDTO;
import com.marcosalencar.parkingManagement.dto.SpotOccupancyRequestDTO;
import com.marcosalencar.parkingManagement.entity.Sector;
import com.marcosalencar.parkingManagement.entity.Spot;
import com.marcosalencar.parkingManagement.entity.SpotOccupancy;
import com.marcosalencar.parkingManagement.repository.SpotOccupancyRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SpotOccupancyService {

    @Autowired
    private SpotOccupancyRepository spotOccupancyRespository;

    @Autowired
    private SpotService spotService;

    @Autowired
    private SectorService sectorService;

    private enum EventType {ENTRY, PARKED, EXIT};

    private String manageEntry(SpotOccupancyRequestDTO dto) throws Exception{
        Optional<SpotOccupancy> plateParked = spotOccupancyRespository.findByLicensePlateAndExitTimeNull(dto.license_plate());

        if(plateParked.isPresent())
            throw new Exception("Já existe um carro estacionado com a placa: " + plateParked.get().getLicensePlate());

        SpotOccupancy spotOccupancy = new SpotOccupancy();
        spotOccupancy.setLicensePlate(dto.license_plate());
        spotOccupancy.setEntryTime(dto.entry_time());
        spotOccupancyRespository.save(spotOccupancy);
        return "Carro com a placa: " + spotOccupancy.getLicensePlate() + " entrou na garagem";
    }

    private String manageParked(SpotOccupancyRequestDTO dto) throws Exception {
        SpotOccupancy lastOccupancy = spotOccupancyRespository.findTopByLicensePlateOrderByIdOccupancyDesc(
                                                            dto.license_plate()).orElseThrow(() ->
                                                            new EntityNotFoundException("Veículo da placa " + dto.license_plate() + " não entrou no estacionamento")
                                                            );

        Spot spot = spotService.getSpotByLatAndLng(dto.lat(), dto.lng());
        Sector sector = spot.getSector();

        if(!sectorService.isSectorOpen(sector))
            throw new Exception("Setor " + sector.getSector() + " está fechado");
        if(sectorService.isSectorFull(sector))
            throw new Exception("Setor " + sector.getSector() + " está lotado");
        if(spot.isOccupied()){
            throw new Exception("Vaga já está ocupada");
        }
        if(lastOccupancy.getExitTime() != null)
            throw new Exception ("Veículo não entrou no estacionamento");

        double pricePerHour = sectorService.calcPricePerHour(sector);
        lastOccupancy.setPricePerHour(pricePerHour);
        sector.setCurrentOccupancy(sector.getCurrentOccupancy()+1);
        spot.setSector(sector);
        spot.setOccupied(true);
        lastOccupancy.setSpot(spot);
        spotOccupancyRespository.save(lastOccupancy);
        return "Veículo estacionado com sucesso";
    }

    private String manageExit(SpotOccupancyRequestDTO dto){
        SpotOccupancy lastOccupancy = spotOccupancyRespository.findTopByLicensePlateOrderByIdOccupancyDesc(
                                                            dto.license_plate()).orElseThrow(() ->
                                                            new EntityNotFoundException("Veículo da placa " + dto.license_plate() + " não entrou no estacionamento")
                                                            );

        
        Spot spot = lastOccupancy.getSpot();
        Sector sector = spot.getSector();

        lastOccupancy.setExitTime(dto.exit_time());
        double finalPrice = calcCurrentPrice(lastOccupancy.getPricePerHour(), lastOccupancy.getEntryTime(), lastOccupancy.getExitTime());
        lastOccupancy.setFinalPrice(finalPrice);
        sector.setCurrentOccupancy(sector.getCurrentOccupancy()-1);
        spot.setSector(sector);
        spot.setOccupied(false);
        lastOccupancy.setSpot(spot);
        spotOccupancyRespository.save(lastOccupancy);
        return "Baixa dada do veículo no estacionamento";
    }

    public double calcCurrentPrice(Double pricePerHour,LocalDateTime entryTime, LocalDateTime referenceTime) {
        Duration duration = Duration.between(entryTime, referenceTime);
        long seconds = duration.getSeconds();
        double decimalHour = (double) seconds/3600;
        BigDecimal roundedValue = BigDecimal.valueOf(decimalHour * pricePerHour)
                                            .setScale(2, RoundingMode.HALF_UP);
        return roundedValue.doubleValue();
    }

    public String fluxControl(SpotOccupancyRequestDTO dto) throws Exception {
        String event = dto.event_type();
        if(event.equals(EventType.ENTRY.toString())){
            return manageEntry(dto);
        } else if(event.equals(EventType.PARKED.toString())) {
            return manageParked(dto);
        } else if (event.equals(EventType.EXIT.toString())){
            return manageExit(dto);
        } else{
            throw new Exception("Tipo de evento inválido");
        }
    }

    public LocalDateTime calcTimeParked(LocalDateTime entryTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT);
        Duration duration = Duration.between(entryTime, now);
        LocalDateTime result = startOfDay.plus(duration);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
        String formattedTime = result.format(formatter);

        return LocalDateTime.parse(formattedTime, formatter);
    }

    public PlateStatusResponseDTO plateStatus(String licensePlate) throws Exception{
        SpotOccupancy spotOccupancy = spotOccupancyRespository.findByLicensePlateAndExitTimeNull(licensePlate).orElseThrow(
                                                               () -> new Exception("Carro com a placa " + licensePlate + " não se encontra estacionado no momento"));

        Double priceUntilNow = calcCurrentPrice(spotOccupancy.getPricePerHour(), spotOccupancy.getEntryTime(), LocalDateTime.now());
        LocalDateTime timeParked = calcTimeParked(spotOccupancy.getEntryTime());
        PlateStatusResponseDTO response = new PlateStatusResponseDTO(spotOccupancy, priceUntilNow, timeParked);
        return response;
    }

}
