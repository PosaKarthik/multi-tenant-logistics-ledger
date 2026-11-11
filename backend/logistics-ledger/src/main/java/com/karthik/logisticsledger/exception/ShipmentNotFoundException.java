package com.karthik.logisticsledger.exception;

public class ShipmentNotFoundException extends RuntimeException{
    public ShipmentNotFoundException(String message){
        super(message);
    }
}
