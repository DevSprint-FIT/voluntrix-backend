package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.TransactionType;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private String orderId;
    private String amount;
    private String currency;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private Long volunteerId;
    private Long sponsorId;
    private Long eventId;
    private String userType;
    private boolean isAnonymous;
    private TransactionType transactionType;
}
