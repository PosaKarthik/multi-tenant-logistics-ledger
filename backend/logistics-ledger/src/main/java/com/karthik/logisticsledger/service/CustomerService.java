package com.karthik.logisticsledger.service;


import com.karthik.logisticsledger.context.TenantContext;
import com.karthik.logisticsledger.dto.CustomerRequest;
import com.karthik.logisticsledger.dto.CustomerResponse;
import com.karthik.logisticsledger.entity.Customer;
import com.karthik.logisticsledger.entity.Tenant;
import com.karthik.logisticsledger.exception.CustomerNotFoundException;
import com.karthik.logisticsledger.exception.TenantNotFoundException;
import com.karthik.logisticsledger.repository.CustomerRepository;
import com.karthik.logisticsledger.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final TenantRepository tenantRepository;
    private final CustomerRepository customerRepository;

    private Long getCurrentTenantId(){

        Long tenantId = TenantContext.getTenantId();

        if(tenantId == null){
            throw new IllegalStateException("No authenticated tenant found");
        }
        return tenantId;
    }

    private CustomerResponse toCustomerResponse(Customer customer){
        CustomerResponse customerResponse = new CustomerResponse();

        customerResponse.setId(customer.getId());
        customerResponse.setName(customer.getName());
        customerResponse.setEmail(customer.getEmail());
        customerResponse.setPhone(customer.getPhone());
        customerResponse.setAddress(customer.getAddress());
        customerResponse.setTenantId(customer.getTenant().getId());
        customerResponse.setCreatedAt(customer.getCreatedAt());
        customerResponse.setUpdatedAt(customer.getUpdatedAt());


        return customerResponse;
    }

    private Customer toCutomer(CustomerRequest customerRequest,Tenant tenant){

        Customer customer = new Customer();

        customer.setName(customerRequest.getName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhone(customerRequest.getPhone());
        customer.setAddress(customerRequest.getAddress());

        customer.setTenant(tenant);

        LocalDateTime now = LocalDateTime.now();

        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);

        return customer;
    }



    public CustomerResponse createCustomer(CustomerRequest customerRequest){

        Long tenantId = getCurrentTenantId();

        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(()-> new TenantNotFoundException("Tenant not found with id: "+ tenantId));


        Customer customer = toCutomer(customerRequest,tenant);

       Customer savedCustomer = customerRepository.save(customer);

       return toCustomerResponse(savedCustomer);

    }

    public List<CustomerResponse> getAllCustomers(){

        Long tenantId = getCurrentTenantId();

        return customerRepository.findAllByTenant_idAndDeletedFalse(tenantId).stream().map(this::toCustomerResponse).toList();
    }


    public CustomerResponse getCustomerById(Long customerId){

        Long tenantId = getCurrentTenantId();

        Customer customer = customerRepository.findByIdAndTenant_IdAndDeletedFalse(customerId,tenantId).orElseThrow(
                ()-> new CustomerNotFoundException("Customer not found with id:  "+customerId)
        );

        return toCustomerResponse(customer);
    }

    public CustomerResponse updateCustomer(Long customerId,CustomerRequest customerRequest){

        Long tenantId = getCurrentTenantId();

        Customer customer = customerRepository.findByIdAndTenant_IdAndDeletedFalse(customerId,tenantId).orElseThrow(
                ()-> new CustomerNotFoundException("Customer not found with id:  "+customerId)
        );

        customer.setName(customerRequest.getName());
        customer.setPhone(customerRequest.getPhone());
        customer.setEmail(customerRequest.getEmail());
        customer.setAddress(customerRequest.getAddress());

        customer.setUpdatedAt(LocalDateTime.now());

        Customer savedCustomer = customerRepository.save(customer);

        return toCustomerResponse(savedCustomer);
    }


    public void deleteCustomer(Long customerId){

        Long tenantId = getCurrentTenantId();

        Customer customer = customerRepository.findByIdAndTenant_IdAndDeletedFalse(customerId,tenantId).orElseThrow(
                ()->new CustomerNotFoundException("Customer not found with id:  "+customerId)
        );

        customer.setDeleted(true);
        customer.setDeletedAt(LocalDateTime.now());

        customerRepository.save(customer);
    }


}
