package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.VolunteerCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerUpdateDTO;
import com.DevSprint.voluntrix_backend.dtos.CategoryDTO;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.CategoryNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.repositories.CategoryRepository;
import com.DevSprint.voluntrix_backend.utils.VolunteerDTOConvert;
import com.DevSprint.voluntrix_backend.utils.VolunteerCategoryEntityDTOConverter;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final VolunteerDTOConvert entityDTOConvert;
    private final VolunteerCategoryEntityDTOConverter volunteerCategoryEntityDTOConverter;
    private final CategoryRepository categoryRepository;

    public VolunteerDTO createVolunteer(VolunteerCreateDTO volunteerCreateDTO) {
        // Check for existing username
        if (volunteerRepository.findByUsername(volunteerCreateDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check for existing email
        if (volunteerRepository.findByEmail(volunteerCreateDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Check for existing phone number
        if (volunteerRepository.findByPhoneNumber(volunteerCreateDTO.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists");
        }

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

    public VolunteerEntity getVolunteerById(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId));
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
        if (volunteerUpdateDTO.getIsEventHost() != null && !volunteer.getIsEventHost()
                && volunteerUpdateDTO.getIsEventHost()) {
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

    public Set<CategoryDTO> getVolunteerCategories(Long volunteerId) {
        VolunteerEntity volunteer = volunteerRepository.findByIdWithCategories(volunteerId)
                .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found with id: " + volunteerId));
        return volunteerCategoryEntityDTOConverter.convertVolunteerCategoriesToCategoryDTOs(volunteer);
    }

    public VolunteerDTO followCategory(Long volunteerId, Long categoryId) {
        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new VolunteerNotFoundException("Volunteer not found with ID: " + volunteerId));

        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));

        volunteer.getFollowedCategories().add(category);
        VolunteerEntity updatedVolunteer = volunteerRepository.save(volunteer);

        return entityDTOConvert.toVolunteerDTO(updatedVolunteer);
    }
}
