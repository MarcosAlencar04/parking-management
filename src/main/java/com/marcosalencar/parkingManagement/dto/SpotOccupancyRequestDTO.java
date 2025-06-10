package com.marcosalencar.parkingManagement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SpotOccupancyRequestDTO(String license_plate,
                                      LocalDateTime entry_time,
                                      String event_type,
                                      BigDecimal lat,
                                      BigDecimal lng,
                                      LocalDateTime exit_time) {
}
