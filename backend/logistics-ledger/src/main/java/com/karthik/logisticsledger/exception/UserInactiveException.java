package com.karthik.logisticsledger.exception;

public class UserInactiveException extends RuntimeException{

    public UserInactiveException(String message){
        super(message);
    }
}
