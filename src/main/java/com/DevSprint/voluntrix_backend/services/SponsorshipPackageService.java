package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.SponsorshipPackageEntity;
import com.DevSprint.voluntrix_backend.repositories.SponsorshipPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SponsorshipPackageService {

    private final SponsorshipPackageRepository repository;

    public List<SponsorshipPackageEntity> getAll() {
        return repository.findAll();
    }

    public Optional<SponsorshipPackageEntity> getById(Long id) {
        return repository.findById(id);
    }

    public SponsorshipPackageEntity create(SponsorshipPackageEntity sponsorshipPackage) {
        return repository.save(sponsorshipPackage);
    }

    public Optional<SponsorshipPackageEntity> update(Long id, SponsorshipPackageEntity updated) {
        return repository.findById(id).map(existing -> {
            updated.setId(existing.getId());
            return repository.save(updated);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}
