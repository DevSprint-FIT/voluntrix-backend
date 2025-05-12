package com.DevSprint.voluntrix_backend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int code;

    public ErrorResponse(String message) {
        this.message = message;
        this.code = HttpStatus.NOT_FOUND.value(); // Default error code
    }
}
