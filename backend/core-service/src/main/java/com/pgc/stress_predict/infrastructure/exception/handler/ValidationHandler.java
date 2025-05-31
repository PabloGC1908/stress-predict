package com.pgc.stress_predict.infrastructure.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ValidationHandler {

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException exceptions) {
        List<String> errores = exceptions.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();

        return new ResponseEntity<>(getErrors(errores), HttpStatus.BAD_REQUEST);
    }

    public Map<String, List<String>> getErrors(List<String> errores) {
        Map<String, List<String>> errorMap = new HashMap<>();
        errorMap.put("errors", errores);

        return errorMap;
    }
}

