package com.karthik.logisticsledger.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {

    @NotBlank(message = "Customer name is required")
    @Size(max = 100,message = "Customer name cannot exceed 100 characters")
    private String name;

    @Email(message = "Please provide a valid email address")
    private String email;

    private String phone;

    private String address;

}
