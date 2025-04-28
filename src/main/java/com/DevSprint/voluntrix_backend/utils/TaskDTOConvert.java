package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.TaskDTO;
import com.DevSprint.voluntrix_backend.dtos.TaskCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.TaskUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.TaskEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskDTOConvert {

    private final ModelMapper modelMapper;

    // Converts a TaskEntity to a TaskDTO
    public TaskDTO toTaskDTO(TaskEntity task) {
        return modelMapper.map(task, TaskDTO.class);
    }

    // Converts a TaskDTO to a TaskEntity
    public TaskEntity toTaskEntity(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, TaskEntity.class);
    }

    // Converts a TaskCreateDTO to a TaskEntity
    public TaskEntity toTaskEntity(TaskCreateDTO taskCreateDTO) {
        return modelMapper.map(taskCreateDTO, TaskEntity.class);
    }

    // Converts a TaskUpdateDTO to a TaskEntity
    public TaskEntity toTaskEntity(TaskUpdateDTO taskUpdateDTO) {
        return modelMapper.map(taskUpdateDTO, TaskEntity.class);
    }

    // Converts a list of TaskEntity to a list of TaskDTO
    public List<TaskDTO> toTaskDTOList(List<TaskEntity> tasks) {
        return tasks.stream()
                .map(entity -> modelMapper.map(entity, TaskDTO.class))
                .collect(Collectors.toList());
    }
}
