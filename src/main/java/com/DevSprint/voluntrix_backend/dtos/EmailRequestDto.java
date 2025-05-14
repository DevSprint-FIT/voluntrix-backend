package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;

@Data
public class EmailRequestDto {

    private String to;
    private String name;
    private String orderId;
    private Double amount; 
    
}
