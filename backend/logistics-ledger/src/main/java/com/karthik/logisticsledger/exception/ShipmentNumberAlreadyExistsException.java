package com.karthik.logisticsledger.exception;

public class ShipmentNumberAlreadyExistsException extends RuntimeException{

    public ShipmentNumberAlreadyExistsException(String message){
        super(message);
    }
}
