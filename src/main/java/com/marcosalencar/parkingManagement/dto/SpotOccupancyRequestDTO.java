package com.marcosalencar.parkingManagement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotOccupancyRequestDTO(@JsonProperty("license_plate") String licensePlate,
                                      @JsonProperty("entry_time") LocalDateTime entryTime,
                                      @JsonProperty("event_type") String eventType,
                                      BigDecimal lat,
                                      BigDecimal lng,
                                      @JsonProperty("exit_time") LocalDateTime exitTime) {
}
