package com.DevSprint.voluntrix_backend.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorResponseDTO {
    //private Long id;
    private String company;
    private boolean isVerified;
    private String jobTitle;
    private String mobileNumber;
    private String name;
    private String email;
    private String website;
    public Object map(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }
}
