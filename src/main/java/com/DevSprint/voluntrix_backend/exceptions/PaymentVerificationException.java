package com.DevSprint.voluntrix_backend.exceptions;

public class PaymentVerificationException extends RuntimeException{
    public PaymentVerificationException(String message) {
        super(message);
    }
}
