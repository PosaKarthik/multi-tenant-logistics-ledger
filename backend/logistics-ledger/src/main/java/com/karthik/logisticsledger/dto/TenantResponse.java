package com.karthik.logisticsledger.dto;

import com.karthik.logisticsledger.entity.TenantStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class TenantResponse {

    private Long id;

    private String name;

    private String code;

    private TenantStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
