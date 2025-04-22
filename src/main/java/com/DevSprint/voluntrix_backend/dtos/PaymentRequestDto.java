package com.DevSprint.voluntrix_backend.dtos;

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
    private String userType;
    private String userId;
    private String eventId;
    private boolean isAnonymous;
}
