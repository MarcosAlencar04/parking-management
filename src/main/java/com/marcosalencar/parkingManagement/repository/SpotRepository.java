package com.marcosalencar.parkingManagement.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcosalencar.parkingManagement.entity.Spot;

public interface SpotRepository extends JpaRepository<Spot, Long>{

    Spot findByLatAndLng(BigDecimal lat, BigDecimal lng);
}
