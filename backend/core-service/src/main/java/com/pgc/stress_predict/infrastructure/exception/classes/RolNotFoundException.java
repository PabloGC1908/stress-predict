package com.pgc.stress_predict.infrastructure.exception.classes;

public class RolNotFoundException extends RuntimeException {
    public RolNotFoundException(String message) {
        super(message);
    }
}
