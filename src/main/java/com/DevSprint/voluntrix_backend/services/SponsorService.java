package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.SponsorCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.utils.SponsorDTOConverter;
import com.DevSprint.voluntrix_backend.exceptions.SponsorNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SponsorService {
    private final SponsorRepository sponsorRepository;
    private final UserRepository userRepository;
    private final SponsorDTOConverter sponsorDTOConverter;

    public List<SponsorDTO> getAllSponsors() {
        List<SponsorEntity> sponsors = sponsorRepository.findAll();
        return sponsorDTOConverter.toSponsorDTOList(sponsors);
    }

    public SponsorDTO createSponsor(SponsorCreateDTO sponsorCreateDTO, Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        // Check if user already has a sponsor profile
        if (sponsorRepository.findByUser(user).isPresent()) {
            throw new IllegalArgumentException("User already has a sponsor profile");
        }
        
        SponsorEntity sponsor = sponsorDTOConverter.toSponsorEntity(sponsorCreateDTO, user);
        SponsorEntity savedSponsor = sponsorRepository.save(sponsor);
        
        // Update user's profile completion status
        user.setIsProfileCompleted(true);
        userRepository.save(user);
        
        return sponsorDTOConverter.toSponsorDTO(savedSponsor);
    }

    public SponsorDTO getSponsorByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        SponsorEntity sponsor = sponsorRepository.findByUser(user)
            .orElseThrow(() -> new SponsorNotFoundException("Sponsor profile not found for user"));
        
        return sponsorDTOConverter.toSponsorDTO(sponsor);
    }

    public SponsorDTO updateSponsorProfile(SponsorUpdateDTO sponsorUpdateDTO, Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        SponsorEntity sponsor = sponsorRepository.findByUser(user)
            .orElseThrow(() -> new SponsorNotFoundException("Sponsor profile not found for user"));
        
        // Update only the fields provided in the DTO (name and email cannot be updated as they come from UserEntity)
        if (sponsorUpdateDTO.getCompany() != null) {
            sponsor.setCompany(sponsorUpdateDTO.getCompany());
        }
        if (sponsorUpdateDTO.getJobTitle() != null) {
            sponsor.setJobTitle(sponsorUpdateDTO.getJobTitle());
        }
        if (sponsorUpdateDTO.getMobileNumber() != null) {
            sponsor.setMobileNumber(sponsorUpdateDTO.getMobileNumber());
        }
        if (sponsorUpdateDTO.getWebsite() != null) {
            sponsor.setWebsite(sponsorUpdateDTO.getWebsite());
        }
        if (sponsorUpdateDTO.getSponsorshipNote() != null) {
            sponsor.setSponsorshipNote(sponsorUpdateDTO.getSponsorshipNote());
        }
        if (sponsorUpdateDTO.getDocumentUrl() != null) {
            sponsor.setDocumentUrl(sponsorUpdateDTO.getDocumentUrl());
        }
        if (sponsorUpdateDTO.getLinkedinProfile() != null) {
            sponsor.setLinkedinProfile(sponsorUpdateDTO.getLinkedinProfile());
        }
        if (sponsorUpdateDTO.getAddress() != null) {
            sponsor.setAddress(sponsorUpdateDTO.getAddress());
        }
        
        SponsorEntity updatedSponsor = sponsorRepository.save(sponsor);
        return sponsorDTOConverter.toSponsorDTO(updatedSponsor);
    }

    public SponsorDTO verifySponsor(Long sponsorId, boolean verified) {
        SponsorEntity sponsor = sponsorRepository.findById(sponsorId)
            .orElseThrow(() -> new SponsorNotFoundException("Sponsor not found with ID: " + sponsorId));
        
        sponsor.setVerified(verified);
        SponsorEntity updatedSponsor = sponsorRepository.save(sponsor);
        return sponsorDTOConverter.toSponsorDTO(updatedSponsor);
    }

    public List<SponsorDTO> getUnverifiedSponsors() {
        List<SponsorEntity> unverifiedSponsors = sponsorRepository.findByVerifiedFalse();
        return sponsorDTOConverter.toSponsorDTOList(unverifiedSponsors);
    }
}
