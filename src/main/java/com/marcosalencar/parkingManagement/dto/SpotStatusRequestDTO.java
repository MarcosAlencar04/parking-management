package com.marcosalencar.parkingManagement.dto;

import java.math.BigDecimal;

public record SpotStatusRequestDTO(BigDecimal lat, 
                                   BigDecimal lng) {

}
