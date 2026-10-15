package com.karthik.logisticsledger.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


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

}
