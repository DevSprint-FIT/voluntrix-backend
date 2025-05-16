package com.DevSprint.voluntrix_backend.exceptions;

public class InvalidUserTypeException extends RuntimeException {
    public InvalidUserTypeException(String message) {
        super(message);
    }
}
