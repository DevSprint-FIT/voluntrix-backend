package com.DevSprint.voluntrix_backend.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String institute;
    private Boolean isAvailable;
    private Boolean isEventHost;
    private String about;
}