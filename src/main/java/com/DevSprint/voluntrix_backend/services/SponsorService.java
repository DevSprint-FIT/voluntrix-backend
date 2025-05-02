package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorResponseDTO;
import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SponsorService {

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<SponsorResponseDTO> getAllSponsors() {
        return sponsorRepository.findAll()
                .stream()
                .map(sponsor -> modelMapper.map(sponsor, SponsorResponseDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<SponsorResponseDTO> getSponsorById(Long id) {
        return sponsorRepository.findById(id)
                .map(sponsor -> modelMapper.map(sponsor, SponsorResponseDTO.class));
    }

    public SponsorResponseDTO createSponsor(SponsorDTO sponsorDTO) {
        SponsorEntity sponsor = modelMapper.map(sponsorDTO, SponsorEntity.class);
        SponsorEntity saved = sponsorRepository.save(sponsor);
        return modelMapper.map(saved, SponsorResponseDTO.class);
    }

    public SponsorResponseDTO updateSponsor(Long id, SponsorDTO sponsorDTO) {
        return sponsorRepository.findById(id)
                .map(existing -> {
                    modelMapper.map(sponsorDTO, existing); // updates fields in-place
                    SponsorEntity updated = sponsorRepository.save(existing);
                    return modelMapper.map(updated, SponsorResponseDTO.class);
                })
                .orElse(null);
    }

    public void deleteSponsor(Long id) {
        sponsorRepository.deleteById(id);
    }
}
