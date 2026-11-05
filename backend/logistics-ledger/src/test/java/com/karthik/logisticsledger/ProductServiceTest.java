package com.karthik.logisticsledger;

import com.karthik.logisticsledger.context.TenantContext;
import com.karthik.logisticsledger.dto.ProductRequest;
import com.karthik.logisticsledger.dto.ProductResponse;
import com.karthik.logisticsledger.entity.Product;
import com.karthik.logisticsledger.entity.Tenant;
import com.karthik.logisticsledger.exception.ProductNotFoundException;
import com.karthik.logisticsledger.exception.ProductSkuAlreadyExistsException;
import com.karthik.logisticsledger.repository.ProductRepository;
import com.karthik.logisticsledger.repository.TenantRepository;
import com.karthik.logisticsledger.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;
    private TenantRepository tenantRepository;


    @BeforeEach
    void setUp(){

        productRepository = mock(ProductRepository.class);
        tenantRepository = mock(TenantRepository.class);

        productService = new ProductService(tenantRepository,productRepository);

        TenantContext.setTenantId(2L);

    }

    @AfterEach
    void tearDown(){
    TenantContext.clear();
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExistForTenant(){

        Long productId = 100L;
        Long tenantId = 2L;

        when(productRepository.findByIdAndTenant_IdAndDeletedFalse(productId,tenantId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,()->productService.getProductById(productId));

        verify(productRepository).findByIdAndTenant_IdAndDeletedFalse(productId,tenantId);
    }

    @Test
    void shouldSoftDeleteProduct(){

        Long productId = 100L;
        Long tenantId = 2L;

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);

        Product product = new Product();
        product.setId(productId);
        product.setTenant(tenant);
        product.setDeleted(false);

        when(productRepository.findByIdAndTenant_IdAndDeletedFalse(productId,tenantId)).thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        assertTrue(product.isDeleted());
        assertNotNull(product.getDeletedAt());

        verify(productRepository,times(1)).save(product);
    }

    @Test
    void shouldPreventUpdatingProductFromAnotherTenant(){

        Long productId = 100L;
        Long tenantId = 2L;

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product");
        productRequest.setSku("SKU-UPDATED");
        productRequest.setDescription("Updated Description");

        when(productRepository.findByIdAndTenant_IdAndDeletedFalse(productId,tenantId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,()->productService.updateProduct(productId,productRequest));

        verify(productRepository).findByIdAndTenant_IdAndDeletedFalse(productId,tenantId);

        verify(productRepository,never()).save(any(Product.class));

    }

    @Test
    void shouldRejectDuplicateSkuForSameTenant(){

        Long tenantId = 2L;

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Laptop");
        productRequest.setSku("SKU-100");
        productRequest.setDescription("Business Laptop");

        when(productRepository.existsByTenant_IdAndSku(tenantId,"SKU-100")).thenReturn(true);

        assertThrows(ProductSkuAlreadyExistsException.class,()->productService.createProduct(productRequest));

        verify(productRepository).existsByTenant_IdAndSku(tenantId,"SKU-100");

        verify(productRepository,never()).save(any(Product.class));

    }

    @Test
    void shouldReturnProductWhenItBelongsToCurrentTenant(){

        Long productId = 100L;
        Long tenantId = 2L;

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);

        Product product = new Product();

        product.setId(productId);
        product.setName("Laptop");
        product.setSku("SKU-100");
        product.setDescription("Business Laptop");
        product.setTenant(tenant);
        product.setDeleted(false);

        when(productRepository.findByIdAndTenant_IdAndDeletedFalse(productId,tenantId)).thenReturn(Optional.of(product));

        ProductResponse productResponse = productService.getProductById(productId);

        assertNotNull(productResponse);
        assertEquals(productId,productResponse.getId());
        assertEquals("Laptop",productResponse.getName());
        assertEquals("SKU-100",productResponse.getSku());
        assertEquals("Business Laptop",productResponse.getDescription());
        assertEquals(tenantId,productResponse.getTenantId());


        verify(productRepository).findByIdAndTenant_IdAndDeletedFalse(productId,tenantId);



    }


    @Test
    void shouldReturnAllProductsForCurrentTenant(){

        Long tenantId = 2L;

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);

        Product product1 = new Product();
        product1.setId(101L);
        product1.setName("Laptop");
        product1.setSku("SKU-101");
        product1.setDescription("Business laptop");
        product1.setTenant(tenant);
        product1.setDeleted(false);

        Product product2 = new Product();
        product2.setId(102L);
        product2.setName("Keyboard");
        product2.setSku("SKU-102");
        product2.setDescription("Wireless keyboard");
        product2.setTenant(tenant);
        product2.setDeleted(false);

        when(productRepository.findAllByTenant_IdAndDeletedFalse(tenantId)).thenReturn(List.of(product1,product2));

        List<ProductResponse> responses = productService.getAllProducts();

        assertEquals(2,responses.size());

        assertEquals(101L,responses.get(0).getId());
        assertEquals("Laptop",responses.get(0).getName());

        assertEquals(102L,responses.get(1).getId());
        assertEquals("Keyboard",responses.get(1).getName());

        assertTrue(responses.stream().allMatch(response -> response
                .getTenantId().equals(tenantId)));

        verify(productRepository).findAllByTenant_IdAndDeletedFalse(tenantId);
    }


}
