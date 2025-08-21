package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SponsorshipPaymentDTO implements Serializable{
    private String orderId;
    private String eventTitle;
    private Long eventId;
    private Long sponsorId;
    private Integer price;
    private String type;
    private String benefits;
    private Integer payableAmount;

}
