package com.karthik.logisticsledger.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(TenantCodeAlreadyExistsException.class)
    public ResponseEntity<String> handleTenantCodeAlreadyExists(TenantCodeAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(TenantNotFoundException.class)
    public ResponseEntity<String> handleTenantNotFound(TenantNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ResponseEntity<String> handleUserEmailAlreadyExists(UserEmailAlreadyExistsException exception){
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

   @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<String> handleUserInactive(UserInactiveException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(ProductNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
