package com.karthik.logisticsledger.exception;

public class TenantCodeAlreadyExistsException extends RuntimeException{
    public TenantCodeAlreadyExistsException(String message){
        super(message);
    }
}
