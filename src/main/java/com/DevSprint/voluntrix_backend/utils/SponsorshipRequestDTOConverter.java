package com.DevSprint.voluntrix_backend.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipRequestDTO;
import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipRequestEntity;
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
}
