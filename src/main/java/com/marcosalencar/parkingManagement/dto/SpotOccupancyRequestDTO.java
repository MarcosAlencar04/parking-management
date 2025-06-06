package com.marcosalencar.parkingManagement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SpotOccupancyRequestDTO(String licensePlate,
                                      LocalDateTime entryTime,
                                      String eventType,
                                      BigDecimal lat,
                                      BigDecimal lng,
                                      LocalDateTime exitTime) {
}
