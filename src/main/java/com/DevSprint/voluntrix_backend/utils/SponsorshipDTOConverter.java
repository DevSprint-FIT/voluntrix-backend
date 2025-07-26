package com.DevSprint.voluntrix_backend.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipEntity;
@Component
public class SponsorshipDTOConverter {
    
    public SponsorshipEntity toSponsorshipEntity(SponsorshipCreateDTO sponsorshipCreateDTO, EventEntity event) {
        SponsorshipEntity sponsorshipEntity = new SponsorshipEntity();
        sponsorshipEntity.setType(sponsorshipCreateDTO.getType());
        sponsorshipEntity.setPrice(sponsorshipCreateDTO.getPrice());
        sponsorshipEntity.setBenefits(sponsorshipCreateDTO.getBenefits());
        sponsorshipEntity.setEvent(event);
        return sponsorshipEntity;
    }

    public SponsorshipDTO toSponsorshipEntity(SponsorshipEntity savedSponsorship) {
        SponsorshipDTO sponsorshipDTO = new SponsorshipDTO();
        sponsorshipDTO.setSponsorshipId(savedSponsorship.getSponsorshipId());
        sponsorshipDTO.setType(savedSponsorship.getType());
        sponsorshipDTO.setPrice(savedSponsorship.getPrice());
        sponsorshipDTO.setBenefits(savedSponsorship.getBenefits());
        sponsorshipDTO.setAvailable(savedSponsorship.isAvailable());
        sponsorshipDTO.setEventId(savedSponsorship.getEvent().getEventId());
        return sponsorshipDTO;
    }

    public List<SponsorshipDTO> toSponsorshipEntityList(List<SponsorshipEntity> sponsorships) {
        return sponsorships.stream()
                .map(this::toSponsorshipEntity)
                .toList();
    }
}