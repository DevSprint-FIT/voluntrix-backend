package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VolunteerDTOConvert {

    private final ModelMapper modelMapper;

    // Converts a VolunteerEntity to a VolunteerDTO  
    public VolunteerDTO toVolunteerDTO(VolunteerEntity volunteer) {
        VolunteerDTO dto = new VolunteerDTO();
        
        // Volunteer-specific fields
        dto.setVolunteerId(volunteer.getVolunteerId());
        dto.setInstitute(volunteer.getInstitute());
        dto.setInstituteEmail(volunteer.getInstituteEmail());
        dto.setIsAvailable(volunteer.getIsAvailable());
        dto.setVolunteerLevel(volunteer.getVolunteerLevel());
        dto.setRewardPoints(volunteer.getRewardPoints());
        dto.setIsEventHost(volunteer.getIsEventHost());
        dto.setJoinedDate(volunteer.getJoinedDate() != null ? volunteer.getJoinedDate().toString() : null);
        dto.setAbout(volunteer.getAbout());
        dto.setPhoneNumber(volunteer.getPhoneNumber());
        dto.setProfilePictureUrl(volunteer.getProfilePictureUrl());
        
        // User-related fields (from UserEntity relationship)
        if (volunteer.getUser() != null) {
            UserEntity user = volunteer.getUser();
            dto.setUserId(user.getUserId());
            dto.setUsername(user.getHandle());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
        }
        
        return dto;
    }

    // Converts a VolunteerDTO to a VolunteerEntity
    public VolunteerEntity toVolunteerEntity(VolunteerDTO volunteerDTO) {
        return modelMapper.map(volunteerDTO, VolunteerEntity.class);
    }

    // Converts a VolunteerCreateDTO to a VolunteerEntity
    public VolunteerEntity toVolunteerEntity(VolunteerCreateDTO volunteerCreateDTO) {
        return modelMapper.map(volunteerCreateDTO, VolunteerEntity.class);
    }

    // Converts a VolunteerCreateDTO to a VolunteerEntity with UserEntity
    public VolunteerEntity toVolunteerEntity(VolunteerCreateDTO volunteerCreateDTO, UserEntity user) {
        VolunteerEntity volunteer = new VolunteerEntity();
        
        // Set volunteer-specific fields
        volunteer.setInstitute(volunteerCreateDTO.getInstitute());
        volunteer.setInstituteEmail(volunteerCreateDTO.getInstituteEmail());
        volunteer.setIsAvailable(volunteerCreateDTO.getIsAvailable());
        volunteer.setIsEventHost(false); // Default to false
        volunteer.setAbout(volunteerCreateDTO.getAbout());
        volunteer.setPhoneNumber(volunteerCreateDTO.getPhoneNumber());
        volunteer.setProfilePictureUrl(volunteerCreateDTO.getProfilePictureUrl());
        
        // Set default values
        volunteer.setVolunteerLevel(1);
        volunteer.setRewardPoints(0);
        
        // Set user relationship
        volunteer.setUser(user);
        
        return volunteer;
    }

    // Converts a VolunteerUpdateDTO to a VolunteerEntity
    public VolunteerEntity toVolunteerEntity(VolunteerUpdateDTO volunteerUpdateDTO) {
        return modelMapper.map(volunteerUpdateDTO, VolunteerEntity.class);
    }

    // Converts a list of VolunteerEntity to a list of VolunteerDTO
    public List<VolunteerDTO> toVolunteerDTOList(List<VolunteerEntity> volunteers) {
        return volunteers.stream()
                .map(entity -> modelMapper.map(entity, VolunteerDTO.class))
                .collect(Collectors.toList());
    }
}