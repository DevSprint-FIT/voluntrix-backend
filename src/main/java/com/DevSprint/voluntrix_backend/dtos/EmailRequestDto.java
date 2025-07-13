package com.DevSprint.voluntrix_backend.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EmailRequestDto {

    @NotBlank(message = "Email recipient is required")
    @Email(message = "A valid email address is required")
    private String to;
    
    @NotBlank(message = "Recipient name is required")
    private String name;
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private Double amount; 
    
}
