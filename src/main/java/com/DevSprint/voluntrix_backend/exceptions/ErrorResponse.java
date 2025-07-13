package com.DevSprint.voluntrix_backend.exceptions;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
}
