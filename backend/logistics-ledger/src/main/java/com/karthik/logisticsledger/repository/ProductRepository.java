package com.karthik.logisticsledger.repository;

import com.karthik.logisticsledger.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findAllByTenant_Id(Long tenantId);

    boolean existsByTenant_IdAndSku(Long tenantId,String sku);

    Optional<Product> findByIdAndTenant_Id(Long id,Long tenantId);
}
