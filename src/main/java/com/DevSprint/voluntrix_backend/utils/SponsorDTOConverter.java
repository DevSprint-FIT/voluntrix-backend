package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.SponsorCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SponsorDTOConverter {

    // SponsorEntity to SponsorDTO
    public SponsorDTO toSponsorDTO(SponsorEntity sponsor) {
        SponsorDTO dto = new SponsorDTO();
        
        dto.setSponsorId(sponsor.getSponsorId());
        
        // Map from associated UserEntity
        dto.setName(sponsor.getUser().getFullName());
        dto.setEmail(sponsor.getUser().getEmail());
        dto.setHandle(sponsor.getUser().getHandle());
        
        // Map sponsor-specific fields
        dto.setCompany(sponsor.getCompany());
        dto.setVerified(sponsor.isVerified());
        dto.setJobTitle(sponsor.getJobTitle());
        dto.setMobileNumber(sponsor.getMobileNumber());
        dto.setWebsite(sponsor.getWebsite());
        dto.setSponsorshipNote(sponsor.getSponsorshipNote());
        dto.setDocumentUrl(sponsor.getDocumentUrl());
        dto.setImageUrl(sponsor.getImageUrl());
        dto.setLinkedinProfile(sponsor.getLinkedinProfile());
        dto.setAddress(sponsor.getAddress());
        dto.setAppliedAt(sponsor.getAppliedAt());
        
        return dto;
    }

    // SponsorCreateDTO to SponsorEntity with User
    public SponsorEntity toSponsorEntity(SponsorCreateDTO dto, UserEntity user) {
        SponsorEntity entity = new SponsorEntity();

        // Note: name and email are inherited from UserEntity relationship
        
        // Set properties from DTO
        entity.setCompany(dto.getCompany());
        entity.setJobTitle(dto.getJobTitle());
        entity.setMobileNumber(dto.getMobileNumber());
        entity.setWebsite(dto.getWebsite());
        entity.setSponsorshipNote(dto.getSponsorshipNote());
        entity.setDocumentUrl(dto.getDocumentUrl());
        entity.setImageUrl(dto.getImageUrl());
        entity.setLinkedinProfile(dto.getLinkedinProfile());
        entity.setAddress(dto.getAddress());
        
        // Set default values
        entity.setVerified(false);
        
        // Set the user relationship
        entity.setUser(user);

        return entity;
    }

    // List<SponsorEntity> to List<SponsorDTO>
    public List<SponsorDTO> toSponsorDTOList(List<SponsorEntity> sponsors) {
        return sponsors.stream()
                .map(this::toSponsorDTO)
                .collect(Collectors.toList());
    }
}
