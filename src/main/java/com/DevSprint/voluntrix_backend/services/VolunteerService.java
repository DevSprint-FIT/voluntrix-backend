package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.utils.VolunteerDTOConvert;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final UserRepository userRepository;
    private final VolunteerDTOConvert entityDTOConvert;

    public VolunteerDTO createVolunteer(VolunteerCreateDTO volunteerCreateDTO, Long userId) {
        // Get user entity by ID
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        // Check if user already has a volunteer profile
        if (volunteerRepository.findByUser(user).isPresent()) {
            throw new IllegalArgumentException("User already has a volunteer profile");
        }
        
        // Check for existing phone number (if provided)
        if (volunteerCreateDTO.getPhoneNumber() != null && 
            volunteerRepository.findByPhoneNumber(volunteerCreateDTO.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists");
        }
    
        VolunteerEntity volunteer = entityDTOConvert.toVolunteerEntity(volunteerCreateDTO, user);
        VolunteerEntity savedVolunteer = volunteerRepository.save(volunteer);
        return entityDTOConvert.toVolunteerDTO(savedVolunteer);
    }

    public List<VolunteerDTO> getAllVolunteers() {
        List<VolunteerEntity> volunteers = volunteerRepository.findAll();
        return entityDTOConvert.toVolunteerDTOList(volunteers);
    }

    // public VolunteerDTO getVolunteerByUsername(String username) {
    //     Optional<VolunteerEntity> volunteer = volunteerRepository.findByUsername(username);
    //     return volunteer.map(entityDTOConvert::toVolunteerDTO)
    //         .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found with username: " + username));
    // }

    public VolunteerEntity getVolunteerById(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId));
    }    

    public VolunteerDTO patchVolunteer(Long volunteerId, VolunteerUpdateDTO volunteerUpdateDTO) {
        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId));
        
        // Update only the volunteer-specific fields provided in the DTO
        if (volunteerUpdateDTO.getInstitute() != null) {
            volunteer.setInstitute(volunteerUpdateDTO.getInstitute());
        }
        if (volunteerUpdateDTO.getInstituteEmail() != null) {
            volunteer.setInstituteEmail(volunteerUpdateDTO.getInstituteEmail());
        }
        if (volunteerUpdateDTO.getIsAvailable() != null) {
            volunteer.setIsAvailable(volunteerUpdateDTO.getIsAvailable());
        }
        if (volunteerUpdateDTO.getIsEventHost() != null && !volunteer.getIsEventHost() && volunteerUpdateDTO.getIsEventHost()) {
            volunteer.setIsEventHost(true); // Promote to host if not already
        }
        if (volunteerUpdateDTO.getAbout() != null) {
            volunteer.setAbout(volunteerUpdateDTO.getAbout());
        }
    
        VolunteerEntity updatedVolunteer = volunteerRepository.save(volunteer);
        return entityDTOConvert.toVolunteerDTO(updatedVolunteer);
    }

    public void deleteVolunteer(Long volunteerId) {
        if (!volunteerRepository.existsById(volunteerId)) {
            throw new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId);
        }
        volunteerRepository.deleteById(volunteerId);
    }
}
