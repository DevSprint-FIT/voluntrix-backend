package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.Sponsorship;
import com.DevSprint.voluntrix_backend.repositories.SponsorshipRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SponsorshipService {

    private final SponsorshipRepository repository;

    public SponsorshipService(SponsorshipRepository repository) {
        this.repository = repository;
    }

    public List<Sponsorship> getAll() {
        return repository.findAll();
    }

    public Optional<Sponsorship> getById(Long id) {
        return repository.findById(id);
    }

    public Sponsorship create(Sponsorship sponsorship) {
        return repository.save(sponsorship);
    }

    public Optional<Sponsorship> update(Long id, Sponsorship updated) {
        return repository.findById(id).map(s -> {
            s.setPackageName(updated.getPackageName());
            s.setDescription(updated.getDescription());
            return repository.save(s);
        });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
