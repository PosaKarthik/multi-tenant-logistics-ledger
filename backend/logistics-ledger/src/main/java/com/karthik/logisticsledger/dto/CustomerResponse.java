package com.karthik.logisticsledger.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private Long tenantId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
