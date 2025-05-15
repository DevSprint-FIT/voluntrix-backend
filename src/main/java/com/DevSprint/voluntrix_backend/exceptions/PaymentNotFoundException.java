package com.DevSprint.voluntrix_backend.exceptions;

public class PaymentNotFoundException extends RuntimeException{
    public PaymentNotFoundException(String message){
        super(message);
    }
}

