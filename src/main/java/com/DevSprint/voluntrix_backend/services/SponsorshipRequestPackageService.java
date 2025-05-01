package com.DevSprint.voluntrix_backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.entities.SponsorshipRequestPackage;
import com.DevSprint.voluntrix_backend.repositories.SponsorshipRequestPackageRepository;

@Service
public class SponsorshipRequestPackageService {
    private final SponsorshipRequestPackageRepository requestPackageRepo;

    public SponsorshipRequestPackageService(SponsorshipRequestPackageRepository requestPackageRepo) {
        this.requestPackageRepo = requestPackageRepo;
    }

    public List<SponsorshipRequestPackage> getAllRequestPackages() {
        return requestPackageRepo.findAll();
    }

    public SponsorshipRequestPackage getById(Long id) {
        return requestPackageRepo.findById(id).orElse(null);
    }

    public SponsorshipRequestPackage create(SponsorshipRequestPackage item) {
        return requestPackageRepo.save(item);
    }

    public void delete(Long id) {
        requestPackageRepo.deleteById(id);
    }
}
