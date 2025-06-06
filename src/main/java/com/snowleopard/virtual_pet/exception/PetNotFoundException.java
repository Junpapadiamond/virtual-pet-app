package com.snowleopard.virtual_pet.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(String message) {
        super(message);
    }
    
    public PetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
