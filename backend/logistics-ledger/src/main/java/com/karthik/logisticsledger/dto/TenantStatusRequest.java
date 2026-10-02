package com.karthik.logisticsledger.dto;

import com.karthik.logisticsledger.entity.TenantStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantStatusRequest {

    @NotNull(message = "Tenant status is required")
    private TenantStatus status;
}
