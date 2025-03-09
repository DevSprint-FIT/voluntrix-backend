package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.entities.Volunteer;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
       this.volunteerRepository = volunteerRepository;
    }

    private VolunteerDTO convertToDTO(Volunteer volunteer) {
        VolunteerDTO volunteerDTO = new VolunteerDTO();
        volunteerDTO.setVolunteerId(volunteer.getVolunteerId());
        volunteerDTO.setFirstName(volunteer.getFirstName());
        volunteerDTO.setLastName(volunteer.getLastName());
        volunteerDTO.setEmail(volunteer.getEmail());
        volunteerDTO.setInstitute(volunteer.getInstitute());
        volunteerDTO.setInterestAreas(volunteer.getInterestAreas());
        volunteerDTO.setAvailabilityStatus(volunteer.getAvailabilityStatus());
        volunteerDTO.setPreferredOrganizations(volunteer.getPreferredOrganizations());
        volunteerDTO.setVolunteerLevel(volunteer.getVolunteerLevel());
        volunteerDTO.setRewardPoints(volunteer.getRewardPoints());
        return volunteerDTO;
    }

    public VolunteerDTO createVolunteer(VolunteerDTO volunteerDTO) {
        Volunteer volunteer = new Volunteer();
        volunteer.setFirstName(volunteerDTO.getFirstName());
        volunteer.setLastName(volunteerDTO.getLastName());
        volunteer.setEmail(volunteerDTO.getEmail());
        volunteer.setInstitute(volunteerDTO.getInstitute());
        volunteer.setInterestAreas(volunteerDTO.getInterestAreas());
        volunteer.setAvailabilityStatus(volunteerDTO.getAvailabilityStatus());
        volunteer.setPreferredOrganizations(volunteerDTO.getPreferredOrganizations());
        volunteer.setVolunteerLevel(1);
        volunteer.setRewardPoints(0);

        Volunteer savedVolunteer = volunteerRepository.save(volunteer);
        return convertToDTO(savedVolunteer);
    }

    public List<VolunteerDTO> getAllVolunteers() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        return volunteers.stream()
                         .map(this::convertToDTO)
                         .collect(Collectors.toList());
    }

    public VolunteerDTO getVolunteerById(Long volunteerId) {
        Optional<Volunteer> volunteer = volunteerRepository.findById(volunteerId);
        return volunteer.map(this::convertToDTO).orElse(null);
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
            return convertToDTO(updatedVolunteer);
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
