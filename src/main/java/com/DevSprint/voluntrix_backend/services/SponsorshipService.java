package com.DevSprint.voluntrix_backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorshipDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipEntity;
import com.DevSprint.voluntrix_backend.exceptions.EventNotFoundException;
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

    public void addSponsorship(SponsorshipCreateDTO sponsorshipCreateDTO) {
        if (sponsorshipCreateDTO.getEventId() == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }

        EventEntity event = eventRepository.findById(sponsorshipCreateDTO.getEventId())
                .orElseThrow(() -> new EventNotFoundException(
                        "Event not found: " + sponsorshipCreateDTO.getEventId()));

        SponsorshipEntity sponsorshipEntity = sponsorshipDTOConvert
                .toSponsorshipEntity(sponsorshipCreateDTO, event);
        sponsorshipRepository.save(sponsorshipEntity);
    }

    public List<SponsorshipDTO> getAllSponsorships() {
        return sponsorshipDTOConvert.toSponsorshipDTOList(sponsorshipRepository.findAll());
    }

    public List<SponsorshipDTO> getAllSponsorshipsByEventId(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventId));

        List<SponsorshipEntity> sponsorships = sponsorshipRepository.findByEvent(event);
        return sponsorshipDTOConvert.toSponsorshipDTOList(sponsorships);
    }
}
