package com.karthik.logisticsledger.dto;

import com.karthik.logisticsledger.entity.ShipmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipmentStatusRequest {


    @NotNull(message = "Shipment status is required")
    private ShipmentStatus status;

}
