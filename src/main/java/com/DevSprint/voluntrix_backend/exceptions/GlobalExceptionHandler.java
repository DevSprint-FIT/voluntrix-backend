package com.DevSprint.voluntrix_backend.exceptions;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
  
    // private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  
    // Handles validation errors from @Valid in DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(new ErrorResponse("VALIDATION_FAILED", errors.toString()), 
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VolunteerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleVolunteerNotFoundException(VolunteerNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
  
    // Handles constraint violations, like @NotNull or @Size on request parameters
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put("error", violation.getMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
  
    // Handles resource not found exceptions (e.g. Organization not found)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    // Handles other illegal argument errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ErrorResponse("ILLEGAL_ARGUMENT", ex.getMessage()), 
                                    HttpStatus.BAD_REQUEST);
    }
    
    // follow system exceptions
    @ExceptionHandler(OrganizationNotFoundException.class)
    public ResponseEntity<Object> handleOrganizationNotFound(OrganizationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(VolunteerAlreadyFollowsOrganizationException.class)
    public ResponseEntity<Object> handleAlreadyFollowing(VolunteerAlreadyFollowsOrganizationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }
     
    // email service exception
    @ExceptionHandler(MailException.class)
    public ResponseEntity<String> handleEmailExceptions(Exception e){
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to send email");
    }
  
    // payment service exceptions
    @ExceptionHandler(PaymentVerificationException.class)
    public ResponseEntity<ErrorResponse> handlePaymentVerificationExceptio(PaymentVerificationException ex) {
        return new ResponseEntity<>(new ErrorResponse("PAYMENT_VERIFICATION_FAILED", ex.getMessage()), 
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse("PAYMENT_NOT_FOUND", ex.getMessage()), 
                                   HttpStatus.NOT_FOUND);
    }
      
    // Catch-all for unknown exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred."), 
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }  
}
