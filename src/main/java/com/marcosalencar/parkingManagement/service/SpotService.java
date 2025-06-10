package com.marcosalencar.parkingManagement.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcosalencar.parkingManagement.entity.Spot;
import com.marcosalencar.parkingManagement.repository.SpotRepository;

@Service
public class SpotService {

    @Autowired
    private SpotRepository spotRepository;

    public List<Spot> getAllSpots(){
        return spotRepository.findAll();
    }

    public Spot getSpotByLatAndLng(BigDecimal lat, BigDecimal lng) {
        return spotRepository.findByLatAndLng(lat, lng);
    }

}
