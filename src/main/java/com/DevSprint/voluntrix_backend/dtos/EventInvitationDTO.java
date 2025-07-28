package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import com.DevSprint.voluntrix_backend.enums.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventInvitationDTO implements Serializable {
    private Long id;
    private Long eventId;
    private Long organizationId;
    private ApplicationStatus applicationStatus;
}

