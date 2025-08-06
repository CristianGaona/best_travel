package com.best.travel.best_travel.util.exceptions;

public class IdNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Record no exist in %s";
    
    public IdNotFoundException(String message) {
        super(String.format(ERROR_MESSAGE, message));
    }
    
}
