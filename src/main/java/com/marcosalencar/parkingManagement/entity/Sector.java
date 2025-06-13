package com.marcosalencar.parkingManagement.entity;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "sector")
@Entity(name = "sector")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "sector")
@ToString
public class Sector {

    @Id
    private Character sector;

    private String currency;
    private Double basePrice;
    private Integer maxCapacity;
    private LocalTime openHour;
    private LocalTime closeHour;
    private Integer  durationLimitMinutes;
    private Integer currentOccupancy;

    public boolean isOpen(){
        LocalTime now = LocalTime.now();
        
        if(openHour.isBefore(closeHour)){
            return !now.isBefore(openHour) && now.isBefore(closeHour);
        }

        return !now.isBefore(openHour) || now.isBefore((closeHour));
    }

    public boolean isFull() {
        double occupancy = (double) currentOccupancy/maxCapacity;
        
        if(occupancy > 1)
            throw new RuntimeException("Ocupação do estacionamento fora da faixa comum");

        return occupancy == 1 ? true : false;
    }
}
