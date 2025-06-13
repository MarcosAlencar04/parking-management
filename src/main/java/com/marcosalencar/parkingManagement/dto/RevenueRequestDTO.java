package com.marcosalencar.parkingManagement.dto;

import java.time.LocalDate;

public record RevenueRequestDTO(LocalDate date,
                                Character sector) {

}
