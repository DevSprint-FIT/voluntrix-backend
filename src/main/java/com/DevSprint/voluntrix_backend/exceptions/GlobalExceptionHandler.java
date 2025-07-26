package com.DevSprint.voluntrix_backend.exceptions;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.mail.MailException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles validation errors from @Valid in DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Return the first error for better UX, or all errors if needed
        String firstErrorMessage = errors.values().iterator().next();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_FAILED", firstErrorMessage));
    }

    // Handles enum conversion errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleEnumConversion(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("ENUM_CONVERSION_FAILED", "Invalid value for enum type."));
    }

    @ExceptionHandler(VolunteerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVolunteerNotFoundException(VolunteerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("VOLUNTEER_NOT_FOUND", ex.getMessage()));
    }

    // Handles constraint violations, like @NotNull or @Size on request parameters
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("RESOURCE_NOT_FOUND", ex.getMessage()));
    }

    // Handles other illegal argument errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("ILLEGAL_ARGUMENT", ex.getMessage()));
    }

    // follow system exceptions
    @ExceptionHandler(OrganizationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrganizationNotFound(OrganizationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("ORGANIZATION_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(VolunteerAlreadyFollowsOrganizationException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyFollowing(VolunteerAlreadyFollowsOrganizationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("ALREADY_FOLLOWING", ex.getMessage()));
    }

    // email service exception
    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponse> handleEmailExceptions(MailException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("MAIL_SERVICE_ERROR", "Failed to send email: " + ex.getMessage()));
    }

    // payment service exceptions
    @ExceptionHandler(PaymentVerificationException.class)
    public ResponseEntity<ErrorResponse> handlePaymentVerificationException(PaymentVerificationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("PAYMENT_VERIFICATION_FAILED", ex.getMessage()));
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("PAYMENT_NOT_FOUND", ex.getMessage()));
    }
    
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException(EventNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("EVENT_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("CATEGORY_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(EventApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventApplicationNotFoundException(EventApplicationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("EVENT_APPLICATION_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateApplicationException(DuplicateApplicationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("EVENT_APPLICATION_ALREADY_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(EventInvitationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventInvitationNotFoundException(EventInvitationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("EVENT_INVITATION_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateInvitationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateInvitationException(DuplicateInvitationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("EVENT_INVITATION_ALREADY_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("BAD_REQUEST", ex.getMessage()));
    }

    @ExceptionHandler(SponsorshipNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSponsorshipNotFoundException(SponsorshipNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("SPONSORSHIP_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(SponsorshipIsNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleSponsorshipIsNotAvailableException(SponsorshipIsNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("SPONSORSHIP_NOT_AVAILABLE", ex.getMessage()));
    }

    @ExceptionHandler(SponsorshipRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSponsorshipRequestNotFoundException(SponsorshipRequestNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("SPONSORSHIP_REQUEST_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleTaskNotFound(TaskNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Authentication 
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("BAD_CREDENTIALS", "Invalid email or password."));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("USER_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("ACCESS_DENIED", ex.getMessage()));
    }

    // Catch-all for unknown exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        System.out.println(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred."));
    }
}