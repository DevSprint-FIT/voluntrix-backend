package com.DevSprint.voluntrix_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponseDTO {
    private String hash;
    private String merchantId;
}
