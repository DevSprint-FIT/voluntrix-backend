package com.DevSprint.voluntrix_backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipEntity;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.SponsorshipNotFoundException;

import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorshipRepository;
import com.DevSprint.voluntrix_backend.utils.SponsorshipDTOConverter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SponsorshipService {

    private final SponsorshipRepository sponsorshipRepository;
    private final EventRepository eventRepository;
    private final SponsorshipDTOConverter sponsorshipDTOConvert;

    public SponsorshipDTO createSponsorship(SponsorshipCreateDTO sponsorshipCreateDTO) {
        EventEntity event = eventRepository.findById(sponsorshipCreateDTO.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + sponsorshipCreateDTO.getEventId()));

        SponsorshipEntity sponsorshipEntity = sponsorshipDTOConvert.toSponsorshipEntity(sponsorshipCreateDTO, event);

        SponsorshipEntity savedSponsorship = sponsorshipRepository.save(sponsorshipEntity);

        return sponsorshipDTOConvert.toSponsorshipEntity(savedSponsorship);
    }

    public SponsorshipDTO getSponsorshipById(Long sponsorshipId) {
        return sponsorshipRepository.findById(sponsorshipId)
                .map(sponsorshipDTOConvert::toSponsorshipEntity)
                .orElseThrow(() -> new SponsorshipNotFoundException("Sponsorship not found with ID: " + sponsorshipId));
    }

    public List<SponsorshipDTO> getSponsorshipsByEventId(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        List<SponsorshipEntity> sponsorships = sponsorshipRepository.findByEventEventId(eventId);
        if (sponsorships.isEmpty()) {
            throw new SponsorshipNotFoundException("No sponsorships found for event with ID: " + eventId);
        }
        return sponsorshipDTOConvert.toSponsorshipEntityList(sponsorships);
    }

    public List<SponsorshipDTO> getAvailableSponsorshipsByEventId(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        List<SponsorshipEntity> sponsorships = sponsorshipRepository.findAvailableSponsorshipsByEventId(eventId);
        if (sponsorships.isEmpty()) {
            throw new SponsorshipNotFoundException("No available sponsorships found for event with ID: " + eventId);
        }
        return sponsorshipDTOConvert.toSponsorshipEntityList(sponsorships);
    }

    public SponsorshipDTO updateSponsorship(Long sponsorshipId, SponsorshipCreateDTO updateDTO) {   
        SponsorshipEntity sponsorshipEntity = sponsorshipRepository.findById(sponsorshipId)
                .orElseThrow(() -> new SponsorshipNotFoundException("Sponsorship not found with ID: " + sponsorshipId));

        sponsorshipEntity.setType(updateDTO.getType());
        sponsorshipEntity.setPrice(updateDTO.getPrice());
        sponsorshipEntity.setBenefits(updateDTO.getBenefits());

        SponsorshipEntity updatedSponsorship = sponsorshipRepository.save(sponsorshipEntity);
        return sponsorshipDTOConvert.toSponsorshipEntity(updatedSponsorship);
    }

    public void deleteSponsorship(Long sponsorshipId) {
        if (!sponsorshipRepository.existsById(sponsorshipId)) {
            throw new SponsorshipNotFoundException("Sponsorship not found with ID: " + sponsorshipId);
        }
        sponsorshipRepository.deleteById(sponsorshipId);
    }

    public SponsorshipDTO updateSponsorshipAvailability(Long sponsorshipId, boolean isAvailable) {
        SponsorshipEntity sponsorship = sponsorshipRepository.findById(sponsorshipId)
                .orElseThrow(() -> new SponsorshipNotFoundException("Sponsorship not found with ID: " + sponsorshipId));
                
        sponsorship.setIsAvailable(isAvailable);
        SponsorshipEntity updatedSponsorship = sponsorshipRepository.save(sponsorship);
        return sponsorshipDTOConvert.toSponsorshipEntity(updatedSponsorship);
    }
}
