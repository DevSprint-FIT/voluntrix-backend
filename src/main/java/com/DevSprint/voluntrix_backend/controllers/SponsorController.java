package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.Entities.Sponsor;
import com.DevSprint.voluntrix_backend.services.SponsorService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/public/sponsors")
public class SponsorController {
    private final SponsorService sponsorService;

    public SponsorController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @GetMapping
    public List<Sponsor> getAllSponsors() {
        return sponsorService.getAllSponsors();
    }

    @GetMapping("/{id}")
    public Sponsor getSponsorById(@PathVariable Long id) {
        return sponsorService.getSponsorById(id);
    }

    @PostMapping
    public Sponsor createSponsor(@RequestBody Sponsor sponsor) {
        return sponsorService.saveSponsor(sponsor);
    }

    @DeleteMapping("/{id}")
    public void deleteSponsor(@PathVariable Long id) {
        sponsorService.deleteSponsor(id);
    }

    
}
