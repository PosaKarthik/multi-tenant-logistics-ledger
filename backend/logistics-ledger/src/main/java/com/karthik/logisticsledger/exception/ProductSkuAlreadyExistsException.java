package com.karthik.logisticsledger.exception;

public class ProductSkuAlreadyExistsException extends RuntimeException{

    public ProductSkuAlreadyExistsException(String message){
        super(message);
    }
}
