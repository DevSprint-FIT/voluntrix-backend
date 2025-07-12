package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.TransactionType;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.validation.ValidPaymentRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
@ValidPaymentRequest
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

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid format")
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

    @NotNull(message = "User type is required")
    private UserType userType;
}