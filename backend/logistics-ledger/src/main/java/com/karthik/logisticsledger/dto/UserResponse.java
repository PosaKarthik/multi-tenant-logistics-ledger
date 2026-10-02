package com.karthik.logisticsledger.dto;

import com.karthik.logisticsledger.entity.UserRole;
import com.karthik.logisticsledger.entity.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private UserRole role;

    private UserStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long tenantId;



}
