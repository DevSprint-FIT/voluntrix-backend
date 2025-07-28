package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.VolunteerCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.dtos.CategoryDTO;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.CategoryNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.repositories.CategoryRepository;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final VolunteerDTOConvert entityDTOConvert;
    private final VolunteerCategoryEntityDTOConverter volunteerCategoryEntityDTOConverter;
    private final CategoryRepository categoryRepository;

    public VolunteerDTO createVolunteer(VolunteerCreateDTO volunteerCreateDTO, Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        // Check if user already has a volunteer profile
        if (volunteerRepository.findByUser(user).isPresent()) {
            throw new IllegalArgumentException("User already has a volunteer profile");
        }
        
        // Validate institute information
        String finalInstitute = null;
        String finalInstituteEmail = null;
        
        if (volunteerCreateDTO.getInstitute() != null && !volunteerCreateDTO.getInstitute().trim().isEmpty()) {
            // Institute is provided, validate that institute email is also provided
            if (volunteerCreateDTO.getInstituteEmail() == null || volunteerCreateDTO.getInstituteEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Institute email is required when institute is provided");
            }
            
            // Check if the institute email was previously verified for this user
            // We assume that if user reaches this point, they have already verified via the separate endpoints
            // The verification happens in InstituteVerificationController before volunteer creation
            finalInstitute = volunteerCreateDTO.getInstitute();
            finalInstituteEmail = volunteerCreateDTO.getInstituteEmail();
        }
        // If institute is not provided or empty, both remain null
        
        // Check for existing phone number (if provided)
        if (volunteerCreateDTO.getPhoneNumber() != null && 
            volunteerRepository.findByPhoneNumber(volunteerCreateDTO.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists");
        }
    
        // Create volunteer entity with validated institute information
        VolunteerEntity volunteer = entityDTOConvert.toVolunteerEntity(volunteerCreateDTO, user, finalInstitute, finalInstituteEmail);
        VolunteerEntity savedVolunteer = volunteerRepository.save(volunteer);
        
        // Update user's profile completion status
        user.setIsProfileCompleted(true);
        userRepository.save(user);
        
        return entityDTOConvert.toVolunteerDTO(savedVolunteer);
    }

    public List<VolunteerDTO> getAllVolunteers() {
        List<VolunteerEntity> volunteers = volunteerRepository.findAllWithUsers();
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

        if (volunteerUpdateDTO.getInstitute() != null) {
            volunteer.setInstitute(volunteerUpdateDTO.getInstitute());
        }
        if (volunteerUpdateDTO.getInstituteEmail() != null) {
            volunteer.setInstituteEmail(volunteerUpdateDTO.getInstituteEmail());
        }
        if (volunteerUpdateDTO.getIsAvailable() != null) {
            volunteer.setIsAvailable(volunteerUpdateDTO.getIsAvailable());
        }
        if (volunteerUpdateDTO.getAbout() != null) {
            volunteer.setAbout(volunteerUpdateDTO.getAbout());
        }

        VolunteerEntity updatedVolunteer = volunteerRepository.save(volunteer);
        return entityDTOConvert.toVolunteerDTO(updatedVolunteer);
    }

    // Update volunteer profile using JWT credentials 
    public VolunteerDTO patchVolunteerProfile(Long userId, VolunteerUpdateDTO volunteerUpdateDTO) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        // Find volunteer profile for this user
        VolunteerEntity volunteer = volunteerRepository.findByUser(user)
            .orElseThrow(() -> new VolunteerNotFoundException("Volunteer profile not found for user"));
        
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
        if (volunteerUpdateDTO.getAbout() != null) {
            volunteer.setAbout(volunteerUpdateDTO.getAbout());
        }
    
        VolunteerEntity updatedVolunteer = volunteerRepository.save(volunteer);
        return entityDTOConvert.toVolunteerDTO(updatedVolunteer);
    }

    // This is a separate endpoint since it's a privilege change
    public VolunteerDTO promoteToEventHost(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        VolunteerEntity volunteer = volunteerRepository.findByUser(user)
            .orElseThrow(() -> new VolunteerNotFoundException("Volunteer profile not found for user"));
        
        // Promote to event host
        if (!volunteer.getIsEventHost()) {
            volunteer.setIsEventHost(true);
            VolunteerEntity updatedVolunteer = volunteerRepository.save(volunteer);
            return entityDTOConvert.toVolunteerDTO(updatedVolunteer);
        } else {
            // Already an event host, just return current state
            return entityDTOConvert.toVolunteerDTO(volunteer);
        }
    }

    public VolunteerDTO getVolunteerByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        VolunteerEntity volunteer = volunteerRepository.findByUser(user)
            .orElseThrow(() -> new VolunteerNotFoundException("Volunteer profile not found for user"));
        
        return entityDTOConvert.toVolunteerDTO(volunteer);
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
