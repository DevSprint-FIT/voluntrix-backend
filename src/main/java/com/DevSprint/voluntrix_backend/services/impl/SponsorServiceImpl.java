package com.DevSprint.voluntrix_backend.services.impl;

import com.DevSprint.voluntrix_backend.Entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.services.SponsorService;
import com.DevSprint.voluntrix_backend.utils.SponsorEntityDTOConvert;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // Performance improving

public class SponsorServiceImpl implements SponsorService {

    private final SponsorRepository sponsorRepository;
    private final SponsorEntityDTOConvert sponsorEntityDTOConvert;

    @Override
    public List<SponsorEntity> getAllSponsors() {
        return sponsorRepository.findAll();
    }

    @Override
    public SponsorEntity getSponsorById(Long id) {
        return sponsorRepository.findById(id).orElse(null);
    }

    @Override
    public void saveSponsor(SponsorDTO sponsorDTO) {
        sponsorRepository.save(sponsorEntityDTOConvert.toSponsorEntity(sponsorDTO));
    }

    @Override
    public void deleteSponsor(Long id) {
        sponsorRepository.deleteById(id);
    }
}
