package com.marcosalencar.parkingManagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcosalencar.parkingManagement.entity.SpotOccupancy;


public interface SpotOccupancyRepository extends JpaRepository<SpotOccupancy, Long>{
    
    Optional<SpotOccupancy> findTopByLicensePlateAndExitTimeIsNullOrderByIdOccupancyDesc(String licensePlate);
    Optional<SpotOccupancy> findByLicensePlateAndExitTimeNull(String licensePlate);
    SpotOccupancy findBySpot_IdSpotAndExitTimeNull(Long id);
    List<SpotOccupancy> findBySpot_Sector_SectorAndEntryTimeBetweenAndExitTimeNotNull(Character sector, LocalDateTime start, LocalDateTime end);
}