package com.karthik.logisticsledger.exception;

public class UserEmailAlreadyExistsException extends  RuntimeException{

    public UserEmailAlreadyExistsException(String message){
        super(message);
    }
}
