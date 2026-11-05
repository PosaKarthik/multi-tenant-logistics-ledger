package com.karthik.logisticsledger;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BasicAssertionsTest {

    @Test
    void addtion(){
        int result = 5 + 5;
        assertEquals(10,result);
    }

    @Test
    void notNull(){

        String productName = "Laptop";

        assertNotNull(productName);
    }

    @Test
    void checkTrue(){

        int quantity = 10;
        assertTrue(quantity > 5);
    }

    @Test
    void checkFalse(){
        int quantity = 10;
        assertFalse(quantity < 5);
    }


}

