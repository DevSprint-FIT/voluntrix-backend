
package com.DevSprint.voluntrix_backend.utils;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;
}


