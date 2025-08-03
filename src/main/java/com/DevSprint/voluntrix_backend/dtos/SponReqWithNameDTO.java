package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import com.DevSprint.voluntrix_backend.enums.SponsorshipRequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponReqWithNameDTO implements Serializable {
    private Long requestId;
    private SponsorshipRequestStatus status;
    private Long sponsorId;
    private Long sponsorshipId;
    private String jobTitle;
    private String sponsorshipName;
    private String company;
}
