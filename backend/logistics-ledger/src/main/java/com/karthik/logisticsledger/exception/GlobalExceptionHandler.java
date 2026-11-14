package com.karthik.logisticsledger.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFound(CustomerNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<String> handleShipmentNotFound(ShipmentNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(ShipmentNumberAlreadyExistsException.class)
    public ResponseEntity<String> handleShipmentNumberAlreadyExists(ShipmentNumberAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception){

        Map<String,String> errors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(
                        error->errors.put(error.getField(),error.getDefaultMessage())
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,String>> handleInvalidRequestBody(HttpMessageNotReadableException exception){

        Throwable cause = exception.getCause();

        while(cause != null && !(cause instanceof InvalidFormatException)){
                cause = cause.getCause();
        }

        if(cause instanceof InvalidFormatException invalidFormatException){
            Class<?> targetType = invalidFormatException.getTargetType();

            if(targetType.isEnum()){
                Object[] allowedValues = targetType.getEnumConstants();
                String allowedValuesMessage = Arrays.stream(allowedValues).map(Object::toString).collect(Collectors.joining(","));

                String fieldName = invalidFormatException.getPath().get(invalidFormatException.getPath().size()-1).getPropertyName();

                Map<String,String> errors = new LinkedHashMap<>();

                errors.put("error","Invalid value for field '"+fieldName+"'.");
                errors.put("message","Allowed values are: "+allowedValuesMessage);

                return ResponseEntity.badRequest().body(errors);
            }
        }


        return ResponseEntity.badRequest().body(
                Map.of("error","Invalid request body. Please check the JSON format and values.")
        );
    }

}
