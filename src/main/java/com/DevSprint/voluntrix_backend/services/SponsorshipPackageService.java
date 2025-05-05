package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipPackageDto;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.SponsorshipPackageEntity;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorshipPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SponsorshipPackageService {

    private final SponsorshipPackageRepository sponsorshipPackageRepository;
    private final EventRepository eventRepository;

    public SponsorshipPackageEntity createFromDto(SponsorshipPackageDto dto) {
        EventEntity event = eventRepository.findById(dto.getEventId())
            .orElseThrow(() -> new RuntimeException("Event not found"));

        SponsorshipPackageEntity entity = new SponsorshipPackageEntity();
        entity.setEvent(event);
        entity.setType(dto.getType());
        entity.setPrice(dto.getPrice());
        entity.setBenefits(dto.getBenefits());
        entity.setAvailable(dto.isAvailable());
        entity.setCreatedAt(LocalDateTime.now());

        return sponsorshipPackageRepository.save(entity);
    }

    public List<SponsorshipPackageEntity> getAll() {
        return sponsorshipPackageRepository.findAll();
    }

    public Optional<SponsorshipPackageEntity> getById(Long id) {
        return sponsorshipPackageRepository.findById(id);
    }

    public Optional<SponsorshipPackageEntity> update(Long id, SponsorshipPackageEntity updated) {
        return sponsorshipPackageRepository.findById(id).map(existing -> {
            existing.setType(updated.getType());
            existing.setPrice(updated.getPrice());
            existing.setBenefits(updated.getBenefits());
            existing.setAvailable(updated.isAvailable());
            return sponsorshipPackageRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (sponsorshipPackageRepository.existsById(id)) {
            sponsorshipPackageRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
