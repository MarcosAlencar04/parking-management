package com.marcosalencar.parkingManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcosalencar.parkingManagement.entity.SpotOccupancy;


public interface SpotOccupancyRepository extends JpaRepository<SpotOccupancy, Long>{
    Optional<SpotOccupancy> findTopByLicensePlateOrderByIdOccupancyDesc(String licensePlate);
    // @Query("SELECT s FROM SpotOccupancy s " + 
    //        "WHERE s.licensePlate = :lp AND s.exitTime IS NULL")
    // Optional<SpotOccupancy> findByLicensePlate(@Param("lp") String licensePlate);
    Optional<SpotOccupancy> findByLicensePlateAndExitTimeNull(String licensePlate);
}