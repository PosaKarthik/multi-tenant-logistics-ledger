package com.karthik.logisticsledger.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class ProductResponse {

    private Long id;
    private String name;
    private String sku;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long tenantId;
}
