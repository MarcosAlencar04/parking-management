package com.marcosalencar.parkingManagement.dto;

import java.time.LocalDateTime;

public record RevenueResponseDTO(Double amount,
                                 String currency,
                                 LocalDateTime timestamp) {
}
