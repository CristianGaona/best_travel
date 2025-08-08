package com.best.travel.best_travel.api.models.api.controllers.error_handler;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.best.travel.best_travel.api.models.responses.BaseErrorResponse;
import com.best.travel.best_travel.api.models.responses.ErrorResponse;
import com.best.travel.best_travel.api.models.responses.ErrorsResponse;
import com.best.travel.best_travel.util.exceptions.IdNotFoundException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {

    @ExceptionHandler(IdNotFoundException.class)
    private BaseErrorResponse handleIdNotFound(IdNotFoundException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private BaseErrorResponse handleIdNotFound(MethodArgumentNotValidException ex) {
        var errors = new ArrayList<String>();
        ex.getAllErrors()
        .forEach(error-> errors.add(error.getDefaultMessage()));
        
        return ErrorsResponse.builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }
}
