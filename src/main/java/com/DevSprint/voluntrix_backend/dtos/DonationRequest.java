package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;

@Data
public class DonationRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private Double amount;
}
