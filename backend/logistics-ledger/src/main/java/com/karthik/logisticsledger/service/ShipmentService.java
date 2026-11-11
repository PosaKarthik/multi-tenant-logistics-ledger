package com.karthik.logisticsledger.service;

import com.karthik.logisticsledger.context.TenantContext;
import com.karthik.logisticsledger.dto.ShipmentRequest;
import com.karthik.logisticsledger.dto.ShipmentResponse;
import com.karthik.logisticsledger.dto.ShipmentStatusRequest;
import com.karthik.logisticsledger.entity.Customer;
import com.karthik.logisticsledger.entity.Shipment;
import com.karthik.logisticsledger.entity.ShipmentStatus;
import com.karthik.logisticsledger.entity.Tenant;
import com.karthik.logisticsledger.exception.CustomerNotFoundException;
import com.karthik.logisticsledger.exception.ShipmentNotFoundException;
import com.karthik.logisticsledger.exception.ShipmentNumberAlreadyExistsException;
import com.karthik.logisticsledger.exception.TenantNotFoundException;
import com.karthik.logisticsledger.repository.CustomerRepository;
import com.karthik.logisticsledger.repository.ShipmentRepository;
import com.karthik.logisticsledger.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final CustomerRepository customerRepository;
    private final TenantRepository tenantRepository;


    private Long getCurrentTenantId(){

        Long tenantId = TenantContext.getTenantId();

        if(tenantId == null){
            throw new IllegalStateException("No authenticated tenant found");
        }

        return tenantId;
    }

    private ShipmentResponse toShipmentResponse(Shipment shipment){
        ShipmentResponse shipmentResponse = new ShipmentResponse();

        shipmentResponse.setId(shipment.getId());
        shipmentResponse.setShipmentNumber(shipment.getShipmentNumber());
        shipmentResponse.setCustomerId(shipment.getCustomer().getId());
        shipmentResponse.setCustomerName(shipment.getCustomer().getName());
        shipmentResponse.setOrigin(shipment.getOrigin());
        shipmentResponse.setDestination(shipment.getDestination());
        shipmentResponse.setShipmentDate(shipment.getShipmentDate());
        shipmentResponse.setExpectedDeliveryDate(shipment.getExpectedDeliveryDate());
        shipmentResponse.setActualDeliveryDate(shipment.getActualDeliveryDate());
        shipmentResponse.setStatus(shipment.getStatus());
        shipmentResponse.setTenantId(shipment.getTenant().getId());
        shipmentResponse.setCreatedAt(shipment.getCreatedAt());
        shipmentResponse.setUpdatedAt(shipment.getUpdatedAt());


        return shipmentResponse;
    }

    private void validShipmentDates(ShipmentRequest shipmentRequest){
        if(shipmentRequest.getExpectedDeliveryDate().isBefore(shipmentRequest.getShipmentDate())){
            throw new IllegalArgumentException("Expected delivery data cannot be before shipment date");
        }
    }

    private void validateStatusTransition(ShipmentStatus currentStatus,ShipmentStatus newStatus){

        boolean isValid = switch (currentStatus){

            case CREATED ->
                newStatus == ShipmentStatus.PICKED_UP || newStatus == ShipmentStatus.CANCELLED;
            case PICKED_UP ->
                newStatus == ShipmentStatus.IN_TRANSIT || newStatus == ShipmentStatus.CANCELLED;
            case IN_TRANSIT ->
                newStatus == ShipmentStatus.DELIVERED || newStatus == ShipmentStatus.CANCELLED;
            case DELIVERED, CANCELLED -> false;
        };

        if(!isValid){
            throw  new IllegalArgumentException(
                    "Invalid shipment status transition from " + currentStatus +" to " + newStatus
            );
        }
    }


    public ShipmentResponse createShipment(ShipmentRequest shipmentRequest){

        validShipmentDates(shipmentRequest);

        Long tenantId = getCurrentTenantId();

        if(shipmentRepository.existsByTenant_IdAndShipmentNumber(tenantId,shipmentRequest.getShipmentNumber())){
            throw new ShipmentNumberAlreadyExistsException("Shipment number already exist");
        }

        Customer customer = customerRepository.findByIdAndTenant_IdAndDeletedFalse(shipmentRequest.getCustomerId(), tenantId).orElseThrow(
                ()->new CustomerNotFoundException("Customer not found")
        );

        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(
                ()->new TenantNotFoundException("Tenant not found")
        );

        Shipment shipment = new Shipment();

        shipment.setShipmentNumber(shipmentRequest.getShipmentNumber());
        shipment.setCustomer(customer);
        shipment.setOrigin(shipmentRequest.getOrigin());
        shipment.setDestination(shipmentRequest.getDestination());
        shipment.setShipmentDate(shipmentRequest.getShipmentDate());
        shipment.setExpectedDeliveryDate(shipmentRequest.getExpectedDeliveryDate());
        shipment.setStatus(ShipmentStatus.CREATED);
        shipment.setTenant(tenant);

        LocalDateTime now = LocalDateTime.now();
        shipment.setCreatedAt(now);
        shipment.setUpdatedAt(now);

        Shipment savedShipment = shipmentRepository.save(shipment);

        return toShipmentResponse(savedShipment);
    }


    public List<ShipmentResponse> getAllShipments(){
        Long tenantId = getCurrentTenantId();
        return shipmentRepository
                            .findAllByTenant_IdAndDeletedFalse(tenantId)
                            .stream()
                            .map(this::toShipmentResponse)
                            .toList();
    }

    public ShipmentResponse getShipmentById(Long shipmentId){
        Long tenantId = getCurrentTenantId();

        Shipment shipment = shipmentRepository.findByIdAndTenant_IdAndDeletedFalse(shipmentId,tenantId).orElseThrow(
                ()-> new ShipmentNotFoundException("Shipment not found")
        );
        return toShipmentResponse(shipment);
    }


    public ShipmentResponse updateShipment(Long shipmentId,ShipmentRequest shipmentRequest){

        validShipmentDates(shipmentRequest);

        Long tenantId = getCurrentTenantId();

        Shipment shipment = shipmentRepository.findByIdAndTenant_IdAndDeletedFalse(shipmentId,tenantId).orElseThrow(
                ()-> new ShipmentNotFoundException("Shipment not found")
        );

        if(shipmentRepository.existsByTenant_IdAndShipmentNumber(tenantId,shipmentRequest.getShipmentNumber()) && !shipment.getShipmentNumber().equals(shipmentRequest.getShipmentNumber())){
            throw new ShipmentNumberAlreadyExistsException("Shipment number already exists");
        }

        Customer  customer = customerRepository.findByIdAndTenant_IdAndDeletedFalse(shipmentRequest.getCustomerId(),tenantId).orElseThrow(
                ()->new CustomerNotFoundException("Customer not found")
        );

        shipment.setShipmentNumber(shipmentRequest.getShipmentNumber());
        shipment.setCustomer(customer);
        shipment.setOrigin(shipmentRequest.getOrigin());
        shipment.setDestination(shipmentRequest.getDestination());
        shipment.setShipmentDate(shipmentRequest.getShipmentDate());
        shipment.setExpectedDeliveryDate(shipmentRequest.getExpectedDeliveryDate());
        shipment.setUpdatedAt(LocalDateTime.now());

        Shipment updatedShipment = shipmentRepository.save(shipment);

        return toShipmentResponse(updatedShipment);
    }

    public void deleteShipment(Long shipmentId){
        Long tenantId = getCurrentTenantId();

        Shipment shipment = shipmentRepository.findByIdAndTenant_IdAndDeletedFalse(shipmentId,tenantId).orElseThrow(
                ()->new ShipmentNotFoundException("Shipment not found")
        );
        shipment.setDeleted(true);
        shipment.setDeletedAt(LocalDateTime.now());

        shipmentRepository.save(shipment);
    }

    public ShipmentResponse updateShipmentStatus(Long shipmentId, ShipmentStatusRequest shipmentStatusRequest){

        Long tenantId = getCurrentTenantId();

        Shipment shipment = shipmentRepository.findByIdAndTenant_IdAndDeletedFalse(shipmentId,tenantId).orElseThrow(
                ()-> new ShipmentNotFoundException("Shipment not found")
        );

        validateStatusTransition(shipment.getStatus(),shipmentStatusRequest.getStatus());

        shipment.setStatus(shipmentStatusRequest.getStatus());

        if(shipmentStatusRequest.getStatus() == ShipmentStatus.DELIVERED){
            shipment.setActualDeliveryDate(LocalDate.now());
        }

        shipment.setUpdatedAt(LocalDateTime.now());

        Shipment savedShipment = shipmentRepository.save(shipment);

        return toShipmentResponse(savedShipment);
    }



}
