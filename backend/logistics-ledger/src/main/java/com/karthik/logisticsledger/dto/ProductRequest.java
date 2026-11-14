package com.karthik.logisticsledger.dto;

import com.karthik.logisticsledger.entity.ProductType;
import com.karthik.logisticsledger.entity.ProductUnit;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "SKU is required")
    @Size(max = 100,message = "SKU must not exceed 100 characters")
    private String sku;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotNull(message = "Product type is required")
    private ProductType type;

    @NotBlank(message = "Product category is required")
    private String category;

    @NotNull(message = "Product unit is required")
    private ProductUnit unit;

    @NotNull(message = "Product unit price is required")
    @DecimalMin(value = "0.01",message = "Product price must be greater than zero")
    @Digits(integer = 10,fraction = 2,message = "Product must have at most 10 integer digits and 2 decimal places")
    private BigDecimal unitPrice;

}
