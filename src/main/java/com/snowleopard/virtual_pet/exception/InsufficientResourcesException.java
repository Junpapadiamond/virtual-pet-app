package com.snowleopard.virtual_pet.exception;

public class InsufficientResourcesException extends RuntimeException {
    public InsufficientResourcesException(String message) {
        super(message);
    }
    
    public InsufficientResourcesException(String message, Throwable cause) {
        super(message, cause);
    }
}
