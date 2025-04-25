package com.DevSprint.voluntrix_backend.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PaymentVerificationException.class)
    public ResponseEntity<ErrorResponse> handlePaymentVerificationExceptio(PaymentVerificationException ex) {
        logger.error("Payment verification failed: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("PAYMENT_VERIFICATION_FAILED", ex.getMessage()), 
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        logger.error("Payment not found: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("PAYMENT_NOT_FOUND", ex.getMessage()), 
                                   HttpStatus.NOT_FOUND);
    }
    
    // Handles validation errors from @Valid in DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        logger.error("Validation errors: {}", errors);
        return new ResponseEntity<>(new ErrorResponse("VALIDATION_FAILED", errors.toString()), 
                                    HttpStatus.BAD_REQUEST);
    }

    // Handles other illegal argument errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        logger.error("Illegal argument: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("ILLEGAL_ARGUMENT", ex.getMessage()), 
                                    HttpStatus.BAD_REQUEST);
    }

    // Catch-all for unknown exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("An error occurred: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred."), 
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }   

}
