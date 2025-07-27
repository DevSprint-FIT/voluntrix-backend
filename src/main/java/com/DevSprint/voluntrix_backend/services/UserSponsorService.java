package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.UserSponsor;
import com.DevSprint.voluntrix_backend.dtos.UserSponsorDTO;
import com.DevSprint.voluntrix_backend.repositories.UserSponsorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserSponsorService {
    
    private final UserSponsorRepository userSponsorRepository;
    
    public List<UserSponsorDTO> getAllActiveSponsors() {
        List<UserSponsor> sponsors = userSponsorRepository.findAllActiveSponsorsOrderedByOnlineStatus();
        return sponsors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserSponsorDTO> searchSponsors(String searchTerm) {
        List<UserSponsor> sponsors = userSponsorRepository.searchActiveSponsors(searchTerm);
        return sponsors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public UserSponsorDTO getSponsorByUsername(String username) {
        UserSponsor sponsor = userSponsorRepository.findByUsername(username)
                .orElse(null);
        return sponsor != null ? convertToDTO(sponsor) : null;
    }
    
    public UserSponsor createSponsor(UserSponsor sponsor) {
        return userSponsorRepository.save(sponsor);
    }
    
    public void updateOnlineStatus(String username, boolean isOnline) {
        userSponsorRepository.findByUsername(username)
                .ifPresent(sponsor -> {
                    sponsor.setIsOnline(isOnline);
                    if (!isOnline) {
                        sponsor.setLastSeen(LocalDateTime.now());
                    }
                    userSponsorRepository.save(sponsor);
                });
    }
    
    private UserSponsorDTO convertToDTO(UserSponsor sponsor) {
        UserSponsorDTO dto = new UserSponsorDTO();
        dto.setId(sponsor.getId());
        dto.setUsername(sponsor.getUsername());
        dto.setFirstName(sponsor.getFirstName());
        dto.setLastName(sponsor.getLastName());
        dto.setEmail(sponsor.getEmail());
        dto.setCompanyName(sponsor.getCompanyName());
        dto.setPhoneNumber(sponsor.getPhoneNumber());
        dto.setBio(sponsor.getBio());
        dto.setIsActive(sponsor.getIsActive());
        dto.setIsOnline(sponsor.getIsOnline());
        dto.setCreatedAt(sponsor.getCreatedAt());
        dto.setLastSeen(sponsor.getLastSeen());
        return dto;
    }
}
