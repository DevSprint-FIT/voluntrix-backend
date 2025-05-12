package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.SponsorRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.SponsorResponseDTO;
import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.exceptions.ResourceNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public SponsorResponseDTO getSponsorById(Long id) {
        SponsorEntity sponsor = sponsorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor with ID " + id + " not found"));
        return modelMapper.map(sponsor, SponsorResponseDTO.class);
    }

    public SponsorResponseDTO createSponsor(SponsorRequestDTO sponsorDTO) {
        SponsorEntity sponsor = modelMapper.map(sponsorDTO, SponsorEntity.class);
        SponsorEntity savedSponsor = sponsorRepository.save(sponsor);
        return modelMapper.map(savedSponsor, SponsorResponseDTO.class);
    }

    public SponsorResponseDTO updateSponsor(Long id, SponsorRequestDTO sponsorDTO) {
        SponsorEntity existingSponsor = sponsorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor with ID " + id + " not found"));

     if(sponsorDTO.getCompany() != null ) {
            existingSponsor.setCompany(sponsorDTO.getCompany());
        }

    
    existingSponsor.setVerified(sponsorDTO.isVerified());
        

    if(sponsorDTO.getJobTitle() != null ) {
           existingSponsor.setJobTitle(sponsorDTO.getJobTitle());
        }

     if(sponsorDTO.getMobileNumber() != null ) {
           existingSponsor.setMobileNumber(sponsorDTO.getMobileNumber());
        }

     if(sponsorDTO.getName() != null ) {
           existingSponsor.setName(sponsorDTO.getName());
        }

     if(sponsorDTO.getEmail() != null ) {
           existingSponsor.setEmail(sponsorDTO.getEmail());
        }

     if(sponsorDTO.getWebsite() != null ) {
           existingSponsor.setWebsite(sponsorDTO.getWebsite());
        }



    
        sponsorRepository.save(existingSponsor);
        return modelMapper.map(existingSponsor, SponsorResponseDTO.class);
    }

    public void deleteSponsor(Long id) {
        SponsorEntity sponsor = sponsorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor with ID " + id + " not found"));
        sponsorRepository.delete(sponsor);
    }
}
