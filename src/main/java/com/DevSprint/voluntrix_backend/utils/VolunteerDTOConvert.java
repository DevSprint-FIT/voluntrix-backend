package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VolunteerDTOConvert {

    private final ModelMapper modelMapper;

    // Converts a VolunteerEntity to a VolunteerDTO  
    public VolunteerDTO toVolunteerDTO(VolunteerEntity volunteer) {
        return modelMapper.map(volunteer, VolunteerDTO.class);
    }

    // Converts a VolunteerDTO to a VolunteerEntity
    public VolunteerEntity toVolunteerEntity(VolunteerDTO volunteerDTO) {
        return modelMapper.map(volunteerDTO, VolunteerEntity.class);
    }

    // Converts a VolunteerCreateDTO to a VolunteerEntity
    public VolunteerEntity toVolunteerEntity(VolunteerCreateDTO volunteerCreateDTO) {
        return modelMapper.map(volunteerCreateDTO, VolunteerEntity.class);
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