package com.karthik.logisticsledger;

public class ProductUtils {

    public boolean isInStock(int quantity){

        if(quantity < 0){
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        return quantity > 0;
    }
}
