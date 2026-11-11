package com.karthik.logisticsledger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ShipmentRequest {

    @NotBlank(message = "Shipment number is required")
    @Size(max = 50,message = "Shipment number cannot exceed 50 characters")
    private String shipmentNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Origin is required")
    private String origin;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Shipment date is required")
    private LocalDate shipmentDate;

    @NotNull(message = "Expected delivery date is required")
    private LocalDate expectedDeliveryDate;

}
