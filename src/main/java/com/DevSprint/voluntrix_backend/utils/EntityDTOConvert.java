package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.VolunteerDTO;
import com.DevSprint.voluntrix_backend.entities.Volunteer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityDTOConvert {

    private final ModelMapper modelMapper;

    // VolunteerEntity to VolunteerDTO
    public VolunteerDTO toVolunteerDTO(Volunteer volunteer) {
        return modelMapper.map(volunteer, VolunteerDTO.class);
    }

    // VolunteerDTO to VolunteerEntity
    public Volunteer toVolunteerEntity(VolunteerDTO volunteerDTO) {
        return modelMapper.map(volunteerDTO, Volunteer.class);
    }

    // List<Volunteer> to List<VolunteerDTO>
    public List<VolunteerDTO> toVolunteerDTOList(List<Volunteer> volunteers) {
        return volunteers.stream()
                .map(entity -> modelMapper.map(entity, VolunteerDTO.class))
                .collect(Collectors.toList());
    }
}
