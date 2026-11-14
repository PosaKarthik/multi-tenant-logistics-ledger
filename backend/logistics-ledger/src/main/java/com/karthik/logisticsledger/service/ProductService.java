package com.karthik.logisticsledger.service;


import com.karthik.logisticsledger.context.TenantContext;
import com.karthik.logisticsledger.dto.ProductRequest;
import com.karthik.logisticsledger.dto.ProductResponse;
import com.karthik.logisticsledger.entity.Product;
import com.karthik.logisticsledger.entity.Tenant;
import com.karthik.logisticsledger.exception.ProductNotFoundException;
import com.karthik.logisticsledger.exception.ProductSkuAlreadyExistsException;
import com.karthik.logisticsledger.exception.TenantNotFoundException;
import com.karthik.logisticsledger.repository.ProductRepository;
import com.karthik.logisticsledger.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final TenantRepository tenantRepository;
    private final ProductRepository productRepository;


    private Long getCurrentTenantId(){
        Long tenantId = TenantContext.getTenantId();

        if(tenantId == null){
            throw new IllegalStateException("No authenticated tenant found");
        }

        return tenantId;
    }

    private void checkSkuAvailability(Long tenantId,String sku){

        if(productRepository.existsByTenant_IdAndSku(tenantId,sku)){
            throw new ProductSkuAlreadyExistsException("SKU already exists for this tenant: " + sku);
        }
    }

    private ProductResponse toProductResponse(Product product){

        ProductResponse productResponse = new ProductResponse();

        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setSku(product.getSku());
        productResponse.setDescription(product.getDescription());
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        productResponse.setTenantId(product.getTenant().getId());
        productResponse.setType(product.getType());
        productResponse.setCategory(product.getCategory());
        productResponse.setUnit(product.getUnit());
        productResponse.setUnitPrice(product.getUnitPrice());

        return productResponse;
    }


    public ProductResponse createProduct(ProductRequest productRequest){

        Long tenantId = getCurrentTenantId();

        checkSkuAvailability(tenantId,productRequest.getSku());

        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(()-> new TenantNotFoundException("Tenant not found with id: "+tenantId));

        Product product = new Product();

        product.setName(productRequest.getName());
        product.setSku(productRequest.getSku());
        product.setDescription(productRequest.getDescription());
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        product.setTenant(tenant);
        product.setType(productRequest.getType());
        product.setCategory(productRequest.getCategory());
        product.setUnit(productRequest.getUnit());
        product.setUnitPrice(productRequest.getUnitPrice());

        Product savedProduct = productRepository.save(product);


        return toProductResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts(){
        Long tenantId = getCurrentTenantId();
        return productRepository.findAllByTenant_IdAndDeletedFalse(tenantId).stream().map(this::toProductResponse).toList();
    }

    public ProductResponse getProductById(Long productId){

        Long tenantId = getCurrentTenantId();

        Product product = productRepository.findByIdAndTenant_IdAndDeletedFalse(productId,tenantId).orElseThrow(()->new ProductNotFoundException("Product not found"));
        return toProductResponse(product);
    }

    public ProductResponse updateProduct(Long productId,ProductRequest productRequest){

        Long tenantId = getCurrentTenantId();

        Product product = productRepository.findByIdAndTenant_IdAndDeletedFalse(productId,tenantId).orElseThrow(()-> new ProductNotFoundException("Product not found"));

        if(productRepository.existsByTenant_IdAndSkuAndIdNot(tenantId,productRequest.getSku(),productId)){
            throw  new ProductSkuAlreadyExistsException("Sku already exists for this tenant: "+ productRequest.getSku());
        }

        product.setName(productRequest.getName());
        product.setSku(productRequest.getSku());
        product.setDescription(productRequest.getDescription());
        product.setUpdatedAt(LocalDateTime.now());
        product.setType(productRequest.getType());
        product.setCategory(productRequest.getCategory());
        product.setUnit(productRequest.getUnit());
        product.setUnitPrice(productRequest.getUnitPrice());

        Product savedProduct = productRepository.save(product);

        return toProductResponse(savedProduct);

    }

    public void deleteProduct(Long productId){

        Long tenantId = getCurrentTenantId();

        Product product = productRepository.findByIdAndTenant_IdAndDeletedFalse(productId,tenantId).orElseThrow(()-> new ProductNotFoundException("Product not found"));

        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());

        productRepository.save(product);
    }
}
