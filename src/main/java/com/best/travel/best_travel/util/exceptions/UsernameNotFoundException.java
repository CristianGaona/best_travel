package com.best.travel.best_travel.util.exceptions;

public class UsernameNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Username not found: %s";

    public UsernameNotFoundException(String username) {
        super(String.format(ERROR_MESSAGE, username));
    }
}
