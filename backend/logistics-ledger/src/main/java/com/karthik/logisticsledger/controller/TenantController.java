package com.karthik.logisticsledger.controller;


import com.karthik.logisticsledger.dto.TenantRequest;
import com.karthik.logisticsledger.dto.TenantResponse;
import com.karthik.logisticsledger.dto.TenantStatusRequest;
import com.karthik.logisticsledger.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;


    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(@RequestBody @Valid TenantRequest tenantRequest){
        TenantResponse response = tenantService.createTenant(tenantRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public TenantResponse getTenantById(@PathVariable Long id){
        return tenantService.getTenantById(id);
    }

    @GetMapping
    public ResponseEntity<List<TenantResponse>> getAllTenants(){
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantResponse> updateTenant(@PathVariable Long id,@Valid @RequestBody TenantRequest tenantRequest){
        return ResponseEntity.ok(tenantService.updateTenant(id,tenantRequest));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TenantResponse> updateTenantStatus(@PathVariable Long id, @RequestBody @Valid TenantStatusRequest tenantStatusRequest){
        return ResponseEntity.ok(tenantService.updateTenantStatus(id,tenantStatusRequest));
    }


}



