package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SponsorService {

    @Autowired
    private SponsorRepository sponsorRepository;

    public List<SponsorEntity> getAllSponsors() {
        return sponsorRepository.findAll();
    }

    public Optional<SponsorEntity> getSponsorById(Long id) {
        return sponsorRepository.findById(id);
    }

    public SponsorEntity createSponsor(SponsorEntity sponsor) {
        return sponsorRepository.save(sponsor);
    }

    public SponsorEntity updateSponsor(Long id, SponsorEntity updatedSponsor) {
        return sponsorRepository.findById(id).map(sponsor -> {
            sponsor.setCompany(updatedSponsor.getCompany());
            sponsor.setVerified(updatedSponsor.isVerified());
            sponsor.setJobTitle(updatedSponsor.getJobTitle());
            sponsor.setMobileNumber(updatedSponsor.getMobileNumber());
            sponsor.setName(updatedSponsor.getName());
            sponsor.setEmail(updatedSponsor.getEmail());
            sponsor.setWebsite(updatedSponsor.getWebsite());
            return sponsorRepository.save(sponsor);
        }).orElse(null);
    }

    public void deleteSponsor(Long id) {
        sponsorRepository.deleteById(id);
    }
}
