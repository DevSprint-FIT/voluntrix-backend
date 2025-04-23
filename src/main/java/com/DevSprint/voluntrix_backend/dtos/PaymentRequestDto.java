package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.TransactionType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class PaymentRequestDto {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Amount is required")
    private String amount;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter ISO currency code")
    private String currency;

    private String firstName;
    private String lastName;

    @NotBlank
    @Email(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}", message = "Email must be valid format")
    private String email;

    private String phone;
    private String address;
    private String city;
    private String country;

    private Long volunteerId;
    private Long sponsorId;
    private Long eventId;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    private boolean isAnonymous;

    @NotBlank(message = "User type is required")
    @Pattern(regexp = "^(VOLUNTEER|SPONSOR|PUBLIC)$", message = "User type must be VOLUNTEER, SPONSOR, or {PUBLIC}")
    private String userType;
    
    @AssertTrue(message = "Volunteer ID is required when user type is VOLUNTEER")
    private boolean isVolunteerIdValid() {
        if ("VOLUNTEER".equals(userType)) {
            return volunteerId != null;
        }
        return true;
    }
    
    @AssertTrue(message = "Sponsor ID is required when user type is SPONSOR")
    private boolean isSponsorIdValid() {
        if ("SPONSOR".equals(userType)) {
            return sponsorId != null;
        }
        return true;
    }
    
    @AssertTrue(message = "Event ID is required for donations and sponsorships")
    private boolean isEventIdValid() {
        if (transactionType != null) {
            return eventId != null;
        }
        return true;
    }

    @AssertTrue(message = "Only one of volunteerId or sponsorId should be provided")
    private boolean isOnlyOneUserProvided() {
        if (volunteerId != null && sponsorId != null) {
            return false;
        }
            return true;
    }
}