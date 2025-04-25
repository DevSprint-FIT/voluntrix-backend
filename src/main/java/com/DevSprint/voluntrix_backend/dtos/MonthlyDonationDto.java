package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;

@Data
public class MonthlyDonationDto {
    private int month;
    private double total;

    public MonthlyDonationDto(int month, double total) {
        this.month = month;
        this.total = total;
    }
}
