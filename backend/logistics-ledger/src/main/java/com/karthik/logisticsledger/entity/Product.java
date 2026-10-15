package com.karthik.logisticsledger.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_tenant_sku",
        columnNames = {"tenant_id","sku"})
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String sku; //business identifier

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "tenant_id",nullable = false)
    private Tenant tenant;
}
