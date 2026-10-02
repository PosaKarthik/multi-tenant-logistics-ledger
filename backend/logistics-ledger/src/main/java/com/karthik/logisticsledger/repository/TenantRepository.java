package com.karthik.logisticsledger.repository;

import com.karthik.logisticsledger.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant,Long> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code,Long id);
}
