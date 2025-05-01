package com.DevSprint.voluntrix_backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.entities.SponsorshipPackage;
import com.DevSprint.voluntrix_backend.repositories.SponsorshipPackageRepository;

@Service
public class SponsorshipPackageService {
    private final SponsorshipPackageRepository packageRepo;

    public SponsorshipPackageService(SponsorshipPackageRepository packageRepo) {
        this.packageRepo = packageRepo;
    }

    public List<SponsorshipPackage> getAllPackages() {
        return packageRepo.findAll();
    }

    public SponsorshipPackage getPackageById(Long id) {
        return packageRepo.findById(id).orElse(null);
    }

    public SponsorshipPackage createPackage(SponsorshipPackage pkg) {
        return packageRepo.save(pkg);
    }

    public void deletePackage(Long id) {
        packageRepo.deleteById(id);
    }
}
