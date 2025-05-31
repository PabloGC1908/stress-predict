package com.pgc.stress_predict.infrastructure.exception.handler;

import com.pgc.stress_predict.infrastructure.exception.classes.RolNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RolNotFoundException.class)
    public ResponseEntity<Object> handleRolNotFoundException(final RolNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
