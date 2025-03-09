package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.entities.Volunteer;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
       this.volunteerRepository = volunteerRepository;
    }

    public Volunteer createVolunteer(VolunteerDTO volunteerDTO) {
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

        return volunteerRepository.save(volunteer);
    }

    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
   }

    public Optional<Volunteer> getVolunteerById(Long volunteerId) {
        return volunteerRepository.findById(volunteerId);
    }

    public Volunteer updateVolunteer(Long volunteerId, VolunteerDTO volunteerDTO) {
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

            return volunteerRepository.save(volunteer);
        } 
        else {
            throw new RuntimeException("Volunteer not found with ID: " + volunteerId);
        }
    }

    public void deleteVolunteer(Long volunteerId) {
        volunteerRepository.deleteById(volunteerId);
    }
}
