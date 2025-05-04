package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.SponsorshipRequestEntity;
import com.DevSprint.voluntrix_backend.repositories.SponsorshipRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SponsorshipRequestService {

    private final SponsorshipRequestRepository repository;

    public List<SponsorshipRequestEntity> getAll() {
        return repository.findAll();
    }

    public Optional<SponsorshipRequestEntity> getById(Long id) {
        return repository.findById(id);
    }

    public SponsorshipRequestEntity create(SponsorshipRequestEntity sponsorshipRequest) {
        return repository.save(sponsorshipRequest);
    }

    public Optional<SponsorshipRequestEntity> update(Long id, SponsorshipRequestEntity updated) {
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

    public List<SponsorshipRequestEntity> getAllRequests() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllRequests'");
    }
}
