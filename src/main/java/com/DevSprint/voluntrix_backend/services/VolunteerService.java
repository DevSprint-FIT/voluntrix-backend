package com.DevSprint.voluntrix_backend.services;

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

    public List<Volunteer> getAllVolunteers() {
         return volunteerRepository.findAll();
    }

    public Volunteer createVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    public Optional<Volunteer> getVolunteerById(Long id) {
        return volunteerRepository.findById(id);
    }
}
