package com.karthik.logisticsledger.repository;

import com.karthik.logisticsledger.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment,Long> {

    List<Shipment> findAllByTenant_IdAndDeletedFalse(Long tenantId);

    Optional<Shipment> findByIdAndTenant_IdAndDeletedFalse(Long shipmentId,Long tenantId);

    boolean existsByTenant_IdAndShipmentNumber(Long tenantId,String shipmentNumber);

}
