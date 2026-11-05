package com.karthik.logisticsledger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.core.parameters.P;

import static org.junit.jupiter.api.Assertions.*;

public class ProductUtilsTest {


    @ParameterizedTest
    @CsvSource({
            "5, true",
            "2, true",
            "0, false",
            "-3, false"
    })
    void shouldCheckStockCorrectly(int quantity, boolean expected) {

        ProductUtils productUtils = new ProductUtils();

        assertEquals(expected, productUtils.isInStock(quantity));
    }


    @Test
    void negativeQuantityShouldThrowException(){

        ProductUtils productUtils = new ProductUtils();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()-> productUtils.isInStock(-3));

        assertEquals("Quantity cannot be negative",exception.getMessage());
    }


    @Test
    void zeroQuantityShouldReturnFalse(){

        ProductUtils productUtils = new ProductUtils();
        boolean result = productUtils.isInStock(0);
        assertFalse(result);
            }


            @Test
            void shouldHandleDifferentPositiveQuantities(){
        ProductUtils productUtils = new ProductUtils();

        assertTrue(productUtils.isInStock(1));
        assertTrue(productUtils.isInStock(5));
        assertTrue(productUtils.isInStock(100));
            }






















//    @Test
//    void positiveQuantityShouldBeInStock(){
//
//        ProductUtils productUtils = new ProductUtils();
//
//        boolean result = productUtils.isInStock(5);
//
//        assertTrue(result);
//    }
//
//    @Test
//    void zeroQuantityShouldNotBeInStock(){
//        ProductUtils productUtils = new ProductUtils();
//
//        boolean result = productUtils.isInStock(0);
//
//        assertFalse(result);
//    }
//
//    @Test
//    void negativeQuantityShouldNotBeInStock(){
//        ProductUtils productUtils = new ProductUtils();
//
//        boolean result = productUtils.isInStock(-3);
//
//        assertFalse(result);
//    }
}
