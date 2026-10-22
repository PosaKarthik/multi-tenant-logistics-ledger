package com.karthik.logisticsledger.repository;

import com.karthik.logisticsledger.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findAllByTenant_IdAndDeletedFalse(Long tenantId);

    boolean existsByTenant_IdAndSku(Long tenantId,String sku);

    Optional<Product> findByIdAndTenant_IdAndDeletedFalse(Long id,Long tenantId);

    boolean existsByTenant_IdAndSkuAndIdNot(Long tenantId,String sku,Long productId);
}
