package org.venkat.freshfruits.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.venkat.freshfruits.exceptions.NotFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class ExceptionControllerAdvise {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException exception) {
        return error(exception, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Map<String, String>> error(final Exception exception, final HttpStatus httpStatus) {
        final String message = Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("errorMessage", message);
        responseMap.put("timeStamp", new Date().toString());
        return new ResponseEntity<>(responseMap, httpStatus);
    }
}
