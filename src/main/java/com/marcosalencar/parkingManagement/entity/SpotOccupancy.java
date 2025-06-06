package com.marcosalencar.parkingManagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "spot_occupancy")
@Entity(name = "spot_occupancy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idOccupancy")
@ToString
public class SpotOccupancy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOccupancy;

    @ManyToOne
    @JoinColumn(name = "id_spot")
    private Spot spot;

    private String licensePlate;
    private LocalDateTime entryTime;
    private Double pricePerHour;
    private LocalDateTime exitTime;
    private Double finalPrice;
}
