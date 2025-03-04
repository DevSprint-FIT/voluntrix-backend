package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.Entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import com.DevSprint.voluntrix_backend.services.SponsorService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor // constructor injection
@RequestMapping("/api/public/sponsors")

public class SponsorController {
    private final SponsorService sponsorService;

    @GetMapping
    public List<SponsorEntity> getAllSponsors() {
        return sponsorService.getAllSponsors();
    }

    @GetMapping("/{id}")
    public SponsorEntity getSponsorById(@PathVariable Long id) {
        return sponsorService.getSponsorById(id);
    }

    @PostMapping
    public ResponseEntity<Void> createSponsor(@RequestBody SponsorDTO sponsorDTO) {
        
        if (sponsorDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);            
        }

        sponsorService.saveSponsor(sponsorDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //sponsorService.saveSponsor(sponsor)

    @DeleteMapping("/{id}")
    public void deleteSponsor(@PathVariable Long id) {
        sponsorService.deleteSponsor(id);
    }

    
}
