package com.DevSprint.voluntrix_backend.exceptions;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
  
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  
    // Handles validation errors from @Valid in DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("VALIDATION_FAILED", errors.toString()));
    }

    @ExceptionHandler(VolunteerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVolunteerNotFoundException(VolunteerNotFoundException ex) {
        logger.warn("Volunteer not found: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("VOLUNTEER_NOT_FOUND", ex.getMessage()));
    }
  
    // Handles constraint violations, like @NotNull or @Size on request parameters
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        logger.warn("Constraint violation: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
            errors.put(
                violation.getPropertyPath().toString(),
                violation.getMessage()
            )
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("CONSTRAINT_VIOLATION", errors.toString()));
    }
  
    // Handles resource not found exceptions (e.g. Organization not found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("RESOURCE_NOT_FOUND", ex.getMessage()));
    }
    
    // Handles other illegal argument errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        logger.warn("Illegal argument: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("ILLEGAL_ARGUMENT", ex.getMessage()));
    }
    
    // follow system exceptions
    @ExceptionHandler(OrganizationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrganizationNotFound(OrganizationNotFoundException ex) {
        logger.warn("Organization not found: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("ORGANIZATION_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(VolunteerAlreadyFollowsOrganizationException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyFollowing(VolunteerAlreadyFollowsOrganizationException ex) {
        logger.warn("Already following: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("ALREADY_FOLLOWING", ex.getMessage()));
    }
     
    // email service exception
    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponse> handleEmailExceptions(MailException ex) {
        logger.error("Mail service error: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("MAIL_SERVICE_ERROR", "Failed to send email: " + ex.getMessage()));
    }
  
    // payment service exceptions
    @ExceptionHandler(PaymentVerificationException.class)
    public ResponseEntity<ErrorResponse> handlePaymentVerificationException(PaymentVerificationException ex) {
        logger.warn("Payment verification failed: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("PAYMENT_VERIFICATION_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        logger.warn("Payment not found: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("PAYMENT_NOT_FOUND", ex.getMessage()));
    }
      
    // Catch-all for unknown exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: ", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred."));
    }  
}