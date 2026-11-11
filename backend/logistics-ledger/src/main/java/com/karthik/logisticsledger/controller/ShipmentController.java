package com.karthik.logisticsledger.controller;

import com.karthik.logisticsledger.dto.ShipmentRequest;
import com.karthik.logisticsledger.dto.ShipmentResponse;
import com.karthik.logisticsledger.dto.ShipmentStatusRequest;
import com.karthik.logisticsledger.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipement(@RequestBody @Valid ShipmentRequest shipmentRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.createShipment(shipmentRequest));
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAllShipments(){
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }


    @GetMapping("/{shipmentId}")
    public ResponseEntity<ShipmentResponse> getShipmentId(@PathVariable Long shipmentId){
        return ResponseEntity.ok(shipmentService.getShipmentById(shipmentId));
    }

    @PutMapping("/{shipmentId}")
    public ResponseEntity<ShipmentResponse> updateShipment(@PathVariable Long shipmentId,@RequestBody @Valid ShipmentRequest shipmentRequest){
        return ResponseEntity.ok(shipmentService.updateShipment(shipmentId,shipmentRequest));
    }

    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteShipment(@PathVariable Long shipmentId){
        shipmentService.deleteShipment(shipmentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{shipmentId}/status")
    public ResponseEntity<ShipmentResponse> updateShipmentStatus(@PathVariable Long shipmentId, @RequestBody @Valid ShipmentStatusRequest shipmentStatusRequest){
        return ResponseEntity.ok(shipmentService.updateShipmentStatus(shipmentId,shipmentStatusRequest));
    }

}
