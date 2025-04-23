package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.Volunteer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityDTOConvert {

    private final ModelMapper modelMapper;

    public VolunteerDTO toVolunteerDTO(Volunteer volunteer) {
        return modelMapper.map(volunteer, VolunteerDTO.class);
    }

    public Volunteer toVolunteerEntity(VolunteerDTO volunteerDTO) {
        return modelMapper.map(volunteerDTO, Volunteer.class);
    }

    public Volunteer toVolunteerEntity(VolunteerCreateDTO volunteerCreateDTO) {
        return modelMapper.map(volunteerCreateDTO, Volunteer.class);
    }

    public Volunteer toVolunteerEntity(VolunteerUpdateDTO volunteerUpdateDTO) {
        return modelMapper.map(volunteerUpdateDTO, Volunteer.class);
    }

    public List<VolunteerDTO> toVolunteerDTOList(List<Volunteer> volunteers) {
        return volunteers.stream()
                .map(entity -> modelMapper.map(entity, VolunteerDTO.class))
                .collect(Collectors.toList());
    }
}