package com.karthik.logisticsledger.dto;

import com.karthik.logisticsledger.entity.ProductType;
import com.karthik.logisticsledger.entity.ProductUnit;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    private ProductType type;
    private String category;
    private ProductUnit unit;
    private BigDecimal unitPrice;

}
