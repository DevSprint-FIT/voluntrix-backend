package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.Entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.dtos.SponsorDTO;
import java.util.List;

public interface SponsorService {
    List<SponsorEntity> getAllSponsors() ;

    SponsorEntity getSponsorById(Long id) ;

    void saveSponsor(SponsorDTO sponsorDTO);

    void deleteSponsor(Long id);
}
