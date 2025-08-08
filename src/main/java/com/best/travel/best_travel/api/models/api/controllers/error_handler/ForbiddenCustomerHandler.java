package com.best.travel.best_travel.api.models.api.controllers.error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.best.travel.best_travel.api.models.responses.BaseErrorResponse;
import com.best.travel.best_travel.api.models.responses.ErrorResponse;
import com.best.travel.best_travel.util.exceptions.ForbiddenCustomerException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenCustomerHandler {
    
    @ExceptionHandler(ForbiddenCustomerException.class)
    private BaseErrorResponse handleIdNotFound(ForbiddenCustomerException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.FORBIDDEN.name())
                .code(HttpStatus.FORBIDDEN.value())
                .build();
    }
}
