package com.DevSprint.voluntrix_backend.utils;

import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.Entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;

@Component
public class SponsorEntityDTOConvert {

    // Sponsor Entity to SponsorDTO
    public SponsorDTO toSponsorDTO(SponsorEntity sponsorEntity) {
        var sponsorDTO = new SponsorDTO();

        sponsorDTO.setId(sponsorEntity.getId());
        sponsorDTO.setSponsorName(sponsorEntity.getSponsorName());

        return sponsorDTO;
    }

    // SponsorDTO to Sponsor Entity
    public SponsorEntity toSponsorEntity(SponsorDTO sponsorDTO){
        var sponsorEntity = new SponsorEntity();

        sponsorEntity.setId(sponsorDTO.getId());
        sponsorEntity.setSponsorName(sponsorDTO.getSponsorName());

        return sponsorEntity;
    }
}
