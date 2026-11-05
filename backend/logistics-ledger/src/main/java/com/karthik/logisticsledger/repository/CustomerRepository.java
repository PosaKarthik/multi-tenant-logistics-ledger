package com.karthik.logisticsledger.repository;

import com.karthik.logisticsledger.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    List<Customer> findAllByTenant_idAndDeletedFalse(Long tenantId);

    Optional<Customer> findByIdAndTenant_IdAndDeletedFalse(Long customerId,Long tenantId);
}
