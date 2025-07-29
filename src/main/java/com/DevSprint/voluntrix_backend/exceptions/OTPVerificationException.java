package com.DevSprint.voluntrix_backend.exceptions;

public class OTPVerificationException extends RuntimeException {
    public OTPVerificationException(String message) {
        super(message);
    }
}
