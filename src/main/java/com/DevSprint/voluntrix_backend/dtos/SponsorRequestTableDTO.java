package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;
import java.time.LocalDate;

import com.DevSprint.voluntrix_backend.enums.SponsorshipPaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorRequestTableDTO implements Serializable {
    private Long requestId;
    private Integer price;
    private String type;
    private String eventTitle;
    private Long eventId;
    private LocalDate eventStartDate;
    private SponsorshipPaymentStatus paymentStatus;
    private Double totalAmountPaid; 
}
