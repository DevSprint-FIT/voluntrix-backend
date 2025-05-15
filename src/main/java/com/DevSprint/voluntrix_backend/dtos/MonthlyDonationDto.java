package com.DevSprint.voluntrix_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyDonationDto {
    private int month;
    private double total;
}
