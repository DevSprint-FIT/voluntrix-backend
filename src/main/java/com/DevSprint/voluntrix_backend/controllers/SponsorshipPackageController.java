package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.SponsorshipPackageDto;
import com.DevSprint.voluntrix_backend.entities.SponsorshipPackageEntity;
import com.DevSprint.voluntrix_backend.services.SponsorshipPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/sponsorship-packages")
@RequiredArgsConstructor
public class SponsorshipPackageController {

    private final SponsorshipPackageService sponsorshipPackageService;

    @PostMapping
    public SponsorshipPackageEntity createPackage(@RequestBody SponsorshipPackageDto dto) {
        return sponsorshipPackageService.createFromDto(dto);
    }

    @GetMapping
    public List<SponsorshipPackageEntity> getAllPackages() {
        return sponsorshipPackageService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SponsorshipPackageEntity> getPackageById(@PathVariable Long id) {
        return sponsorshipPackageService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SponsorshipPackageEntity> updatePackage(@PathVariable Long id, @RequestBody SponsorshipPackageEntity updatedPackage) {
        return sponsorshipPackageService.update(id, updatedPackage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        return sponsorshipPackageService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
