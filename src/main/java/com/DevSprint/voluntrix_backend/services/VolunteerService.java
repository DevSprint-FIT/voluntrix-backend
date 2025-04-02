package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.entities.Volunteer;
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

    public VolunteerDTO createVolunteer(VolunteerDTO volunteerDTO) {
        Volunteer volunteer = entityDTOConvert.toVolunteerEntity(volunteerDTO);
        Volunteer savedVolunteer = volunteerRepository.save(volunteer);
        return entityDTOConvert.toVolunteerDTO(savedVolunteer);
    }

    public List<VolunteerDTO> getAllVolunteers() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        return entityDTOConvert.toVolunteerDTOList(volunteers);
    }

    public VolunteerDTO getVolunteerById(Long volunteerId) {
        Optional<Volunteer> volunteer = volunteerRepository.findById(volunteerId);
        return volunteer.map(entityDTOConvert::toVolunteerDTO).orElse(null);
    }

    public VolunteerDTO updateVolunteer(Long volunteerId, VolunteerDTO volunteerDTO) {
        Optional<Volunteer> existingVolunteer = volunteerRepository.findById(volunteerId);

        if (existingVolunteer.isPresent()) {
            Volunteer volunteer = existingVolunteer.get();
            volunteer.setFirstName(volunteerDTO.getFirstName());
            volunteer.setLastName(volunteerDTO.getLastName());
            volunteer.setEmail(volunteerDTO.getEmail());
            volunteer.setInstitute(volunteerDTO.getInstitute());
            volunteer.setInterestAreas(volunteerDTO.getInterestAreas());
            volunteer.setAvailabilityStatus(volunteerDTO.getAvailabilityStatus());
            volunteer.setPreferredOrganizations(volunteerDTO.getPreferredOrganizations());

            Volunteer updatedVolunteer = volunteerRepository.save(volunteer);
            return entityDTOConvert.toVolunteerDTO(updatedVolunteer);
        } else {
            throw new RuntimeException("Volunteer not found with ID: " + volunteerId);
        }
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
