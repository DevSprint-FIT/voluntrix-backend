package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SponsorshipDTO implements Serializable {

    private Long sponsorshipId;
    private String sponsorshipName;
    private Integer sponsorshipAmount;

    private Long eventId;
}
