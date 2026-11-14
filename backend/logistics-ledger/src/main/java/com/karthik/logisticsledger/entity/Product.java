package com.karthik.logisticsledger.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_tenant_sku",
        columnNames = {"tenant_id","sku"})
},name = "products")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductUnit unit;

    @Column(nullable = false,precision = 12,scale = 2)
    private BigDecimal unitPrice;


    @Column(nullable = false)
    private boolean deleted = false;

    private LocalDateTime deletedAt;
}
