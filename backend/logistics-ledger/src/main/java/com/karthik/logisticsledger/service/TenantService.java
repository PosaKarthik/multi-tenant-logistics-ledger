package com.karthik.logisticsledger.service;


import com.karthik.logisticsledger.dto.TenantRequest;
import com.karthik.logisticsledger.dto.TenantResponse;
import com.karthik.logisticsledger.dto.TenantStatusRequest;
import com.karthik.logisticsledger.entity.Tenant;
import com.karthik.logisticsledger.entity.TenantStatus;
import com.karthik.logisticsledger.exception.TenantCodeAlreadyExistsException;
import com.karthik.logisticsledger.exception.TenantNotFoundException;
import com.karthik.logisticsledger.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;


    public TenantResponse toResponse(Tenant tenant){

        TenantResponse tenantResponse = new TenantResponse();

        tenantResponse.setId(tenant.getId());
        tenantResponse.setName(tenant.getName());
        tenantResponse.setCode(tenant.getCode());
        tenantResponse.setStatus(tenant.getStatus());
        tenantResponse.setCreatedAt(tenant.getCreatedAt());
        tenantResponse.setUpdatedAt(tenant.getUpdatedAt());

        return  tenantResponse;

    }


    public TenantResponse createTenant(TenantRequest tenantRequest){

        if(tenantRepository.existsByCode(tenantRequest.getCode())){
            throw  new TenantCodeAlreadyExistsException("Tenant code already exists");
        }
LocalDateTime now = LocalDateTime.now();
        Tenant tenant = new Tenant();
        tenant.setName(tenantRequest.getName());
        tenant.setCode(tenantRequest.getCode());

        tenant.setStatus(TenantStatus.ACTIVE);
        tenant.setCreatedAt(now);
        tenant.setUpdatedAt(now);

        Tenant savedTenant = tenantRepository.save(tenant);

        return toResponse(savedTenant);
    }


    public TenantResponse getTenantById(Long id){

        Tenant tenant = tenantRepository.findById(id).orElseThrow(
                () -> new TenantNotFoundException("Tenant not found with id: " + id)
        );

        return toResponse(tenant);
    }

    public List<TenantResponse> getAllTenants(){

       return tenantRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TenantResponse updateTenant(Long id,TenantRequest tenantRequest){

        Tenant tenant = tenantRepository.findById(id).orElseThrow(()->new TenantNotFoundException("Tenant not found with id: "+id));

        if(tenantRepository.existsByCodeAndIdNot(tenantRequest.getCode(), id)){
            throw new TenantCodeAlreadyExistsException("Tenant code already exists: " + tenantRequest.getCode());
        }

        tenant.setName(tenantRequest.getName());
            tenant.setCode(tenantRequest.getCode());
            tenant.setUpdatedAt(LocalDateTime.now());
            Tenant updatedTenant = tenantRepository.save(tenant);
            return  toResponse(updatedTenant);
    }

    public TenantResponse updateTenantStatus(Long id, TenantStatusRequest tenantStatusRequest){

        Tenant tenant = tenantRepository.findById(id).orElseThrow(()->new TenantNotFoundException("Tenant not found with id: " + id));

        tenant.setStatus(tenantStatusRequest.getStatus());
        tenant.setUpdatedAt(LocalDateTime.now());

        Tenant updatedTenant = tenantRepository.save(tenant);

        return toResponse(updatedTenant);

    }

}
