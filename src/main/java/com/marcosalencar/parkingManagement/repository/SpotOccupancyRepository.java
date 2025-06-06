package com.marcosalencar.parkingManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcosalencar.parkingManagement.entity.SpotOccupancy;

public interface SpotOccupancyRepository extends JpaRepository<SpotOccupancy, Long>{

}
