package com.DevSprint.voluntrix_backend.utils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.SponsorRequestTableDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipRequestDTO;
import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipRequestEntity;
import com.DevSprint.voluntrix_backend.enums.SponsorshipPaymentStatus;
import com.DevSprint.voluntrix_backend.enums.SponsorshipRequestStatus;

@Component
public class SponsorshipRequestDTOConverter {

    public static SponsorshipRequestEntity toSponsorshipRequestEntity(SponsorEntity sponsor, SponsorshipEntity sponsorship) {
        SponsorshipRequestEntity requestEntity = new SponsorshipRequestEntity();
        requestEntity.setSponsor(sponsor);
        requestEntity.setSponsorship(sponsorship);
        requestEntity.setStatus(SponsorshipRequestStatus.PENDING);

        return requestEntity;
    }

    public SponsorshipRequestDTO toSponsorshipRequestDTO(SponsorshipRequestEntity savedRequest) {
        SponsorshipRequestDTO dto = new SponsorshipRequestDTO();
        dto.setRequestId(savedRequest.getRequestId());
        dto.setStatus(savedRequest.getStatus());
        dto.setSponsorId(savedRequest.getSponsor().getSponsorId());
        dto.setSponsorshipId(savedRequest.getSponsorship().getSponsorshipId());

        return dto;
    }

    public List<SponsorshipRequestDTO> toSponsorshipRequestDTOList(List<SponsorshipRequestEntity> requests) {
        return requests.stream()
                .map(this::toSponsorshipRequestDTO)
                .toList();
    }

    public List<SponsorRequestTableDTO> toSponsorRequestTableDTOList(List<Object[]> sponsorshipDetails) {
        return sponsorshipDetails.stream()
                .map(detail -> {
                    Long eventId = (Long) detail[0];
                    String eventTitle = (String) detail[1];
                    LocalDate eventStartDate = (LocalDate) detail[2];
                    String type = (String) detail[3];
                    Integer price = (Integer) detail[4];
                    Long requestId = (Long) detail[5];


                    SponsorRequestTableDTO dto = new SponsorRequestTableDTO();
                    dto.setEventId(eventId);
                    dto.setEventTitle(eventTitle);
                    dto.setEventStartDate(eventStartDate);
                    dto.setType(type);
                    dto.setPrice(price);
                    dto.setRequestId(requestId);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<SponsorRequestTableDTO> toSponsorRequestTableDTOList(List<Object[]> sponsorshipDetails,
            SponsorshipPaymentStatus paymentStatus, Double totalAmountPaid) {
                return sponsorshipDetails.stream()
                .map(detail -> {
                    Long eventId = (Long) detail[0];
                    String eventTitle = (String) detail[1];
                    LocalDate eventStartDate = (LocalDate) detail[2];
                    String type = (String) detail[3];
                    Integer price = (Integer) detail[4];
                    Long requestId = (Long) detail[5];

                    SponsorRequestTableDTO dto = new SponsorRequestTableDTO();
                    dto.setEventId(eventId);
                    dto.setEventTitle(eventTitle);
                    dto.setEventStartDate(eventStartDate);
                    dto.setType(type);
                    dto.setPrice(price);
                    dto.setRequestId(requestId);
                    dto.setPaymentStatus(paymentStatus);
                    dto.setTotalAmountPaid(totalAmountPaid);

                    return dto;
                })
                .collect(Collectors.toList());

    }
}
