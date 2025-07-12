package com.DevSprint.voluntrix_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecaptchaResponseDTO {
    private boolean success;
    private String challenge_ts;
    private String hostname;
    private List<String> errorCodes;
}
