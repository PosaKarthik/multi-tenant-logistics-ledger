package com.karthik.logisticsledger.dto;

import com.karthik.logisticsledger.entity.ShipmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShipmentResponse {

    private Long id;

    private String shipmentNumber;

    private Long customerId;

    private String customerName;

    private String origin;

    private String destination;

    private LocalDate shipmentDate;

    private LocalDate expectedDeliveryDate;

    private LocalDate actualDeliveryDate;

    private ShipmentStatus status;

    private Long tenantId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
