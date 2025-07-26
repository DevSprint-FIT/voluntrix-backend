package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SponsorshipCreateDTO implements Serializable {

    private String sponsorshipName;
    private Integer sponsorshipAmount;

    private Long eventId;
}
