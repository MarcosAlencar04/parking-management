package com.marcosalencar.parkingManagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlateStatusRequestDTO(@JsonProperty("license_plate") String licensePlate) {

}
