package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.utils.EntityDTOConvert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final EntityDTOConvert entityDTOConvert;

    public VolunteerService(VolunteerRepository volunteerRepository, EntityDTOConvert entityDTOConvert) {
        this.volunteerRepository = volunteerRepository;
        this.entityDTOConvert = entityDTOConvert;
    }

    public VolunteerDTO createVolunteer(VolunteerCreateDTO volunteerCreateDTO) {
        VolunteerEntity volunteer = entityDTOConvert.toVolunteerEntity(volunteerCreateDTO);

        VolunteerEntity savedVolunteer = volunteerRepository.save(volunteer);
        return entityDTOConvert.toVolunteerDTO(savedVolunteer);
    }

    public List<VolunteerDTO> getAllVolunteers() {
        List<VolunteerEntity> volunteers = volunteerRepository.findAll();
        return entityDTOConvert.toVolunteerDTOList(volunteers);
    }

    public VolunteerDTO getVolunteerByUsername(String username) {
        Optional<VolunteerEntity> volunteer = volunteerRepository.findByUsername(username);
        return volunteer.map(entityDTOConvert::toVolunteerDTO)
            .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found with username: " + username));
    }

    public VolunteerDTO patchVolunteer(Long volunteerId, VolunteerUpdateDTO volunteerUpdateDTO) {
        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId));
    
        // Update only the fields provided in the DTO
        if (volunteerUpdateDTO.getFirstName() != null) {
            volunteer.setFirstName(volunteerUpdateDTO.getFirstName());
        }
        if (volunteerUpdateDTO.getLastName() != null) {
            volunteer.setLastName(volunteerUpdateDTO.getLastName());
        }
        if (volunteerUpdateDTO.getEmail() != null) {
            volunteer.setEmail(volunteerUpdateDTO.getEmail());
        }
        if (volunteerUpdateDTO.getInstitute() != null) {
            volunteer.setInstitute(volunteerUpdateDTO.getInstitute());
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

    public boolean deleteVolunteer(Long volunteerId) {
        if (volunteerRepository.existsById(volunteerId)) {
            volunteerRepository.deleteById(volunteerId);
            return true;
        } else {
            return false;
        }
    }
}
