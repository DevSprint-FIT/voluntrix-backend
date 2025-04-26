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
public class EntityDTOConvert {

    private final ModelMapper modelMapper;

    public VolunteerDTO toVolunteerDTO(VolunteerEntity volunteer) {
        return modelMapper.map(volunteer, VolunteerDTO.class);
    }

    public VolunteerEntity toVolunteerEntity(VolunteerDTO volunteerDTO) {
        return modelMapper.map(volunteerDTO, VolunteerEntity.class);
    }

    public VolunteerEntity toVolunteerEntity(VolunteerCreateDTO volunteerCreateDTO) {
        return modelMapper.map(volunteerCreateDTO, VolunteerEntity.class);
    }

    public VolunteerEntity toVolunteerEntity(VolunteerUpdateDTO volunteerUpdateDTO) {
        return modelMapper.map(volunteerUpdateDTO, VolunteerEntity.class);
    }

    public List<VolunteerDTO> toVolunteerDTOList(List<VolunteerEntity> volunteers) {
        return volunteers.stream()
                .map(entity -> modelMapper.map(entity, VolunteerDTO.class))
                .collect(Collectors.toList());
    }
}