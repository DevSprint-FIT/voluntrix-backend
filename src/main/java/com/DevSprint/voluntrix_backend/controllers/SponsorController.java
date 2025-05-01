package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.services.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/sponsors")

public class SponsorController {

    @Autowired
    private SponsorService sponsorService;

    @GetMapping
    public List<SponsorEntity> getAllSponsors() {
        return sponsorService.getAllSponsors();
    }

    @GetMapping("/{id}")
    public Optional<SponsorEntity> getSponsorById(@PathVariable Long id) {
        return sponsorService.getSponsorById(id);
    }

    @PostMapping
    public SponsorEntity createSponsor(@RequestBody SponsorEntity sponsor) {
        return sponsorService.createSponsor(sponsor);
    }

    @PutMapping("/{id}")
    public SponsorEntity updateSponsor(@PathVariable Long id, @RequestBody SponsorEntity sponsor) {
        return sponsorService.updateSponsor(id, sponsor);
    }

    @DeleteMapping("/{id}")
    public void deleteSponsor(@PathVariable Long id) {
        sponsorService.deleteSponsor(id);
    }
}
