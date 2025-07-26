package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SponsorshipDTOConverter {

    private final ModelMapper modelMapper;

    // SponsorshipEntity to SponsorshipDTO
    public SponsorshipDTO toSponsorshipDTO(SponsorshipEntity sponsorshipEntity) {
        return modelMapper.map(sponsorshipEntity, SponsorshipDTO.class);
    }

    // SponsorshipCreateDTO to SponsorshipEntity
    public SponsorshipEntity toSponsorshipEntity(SponsorshipCreateDTO sponsorshipCreateDTO, EventEntity eventEntity) {
        SponsorshipEntity sponsorshipEntity = new SponsorshipEntity();

        sponsorshipEntity.setSponsorshipName(sponsorshipCreateDTO.getSponsorshipName());
        sponsorshipEntity.setSponsorshipAmount(sponsorshipCreateDTO.getSponsorshipAmount());

        sponsorshipEntity.setEvent(eventEntity);

        return sponsorshipEntity;
    }

    // List<SponsorshipEntity> to List<SponsorshipDTO>
    public List<SponsorshipDTO> toSponsorshipDTOList(List<SponsorshipEntity> sponsorshipEntityList) {
        return sponsorshipEntityList.stream().map(entity -> modelMapper.map(entity, SponsorshipDTO.class))
                .collect(Collectors.toList());
    }
}