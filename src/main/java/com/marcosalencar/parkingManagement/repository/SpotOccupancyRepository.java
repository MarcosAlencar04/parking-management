package com.marcosalencar.parkingManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcosalencar.parkingManagement.entity.SpotOccupancy;


public interface SpotOccupancyRepository extends JpaRepository<SpotOccupancy, Long>{
    
    Optional<SpotOccupancy> findTopByLicensePlateAndExitTimeIsNullOrderByIdOccupancyDesc(String licensePlate);
    Optional<SpotOccupancy> findByLicensePlateAndExitTimeNull(String licensePlate);
    SpotOccupancy findBySpot_IdSpotAndExitTimeNull(Long id);
}