package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.Entities.Sponsor;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SponsorService {
    private final SponsorRepository sponsorRepository;

    public SponsorService(SponsorRepository sponsorRepository) {
        this.sponsorRepository = sponsorRepository;
    }

    public List<Sponsor> getAllSponsors() {
        return sponsorRepository.findAll();
    }

    public Sponsor getSponsorById(Long id) {
        return sponsorRepository.findById(id).orElse(null);
    }

    public Sponsor saveSponsor(Sponsor sponsor) {
        return sponsorRepository.save(sponsor);
    }

    public void deleteSponsor(Long id) {
        sponsorRepository.deleteById(id);
    }
}
