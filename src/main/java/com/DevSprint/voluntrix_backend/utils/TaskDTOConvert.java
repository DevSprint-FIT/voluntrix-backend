package com.DevSprint.voluntrix_backend.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.DevSprint.voluntrix_backend.enums.TaskStatus;
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
        TaskDTO dto = modelMapper.map(task, TaskDTO.class);

        // Manually map nested fields
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getVolunteerId());
            dto.setAssigneeUsername(task.getAssignee().getUsername());
        }

        if (task.getEvent() != null) {
            dto.setEventId(task.getEvent().getEventId());
            dto.setEventTitle(task.getEvent().getEventTitle());
        }

        return dto;
    }

    // Converts a TaskDTO to a TaskEntity
    public TaskEntity toTaskEntity(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, TaskEntity.class);
    }

    // Converts a TaskCreateDTO to a TaskEntity
    public TaskEntity toTaskEntity(TaskCreateDTO taskCreateDTO) {
        TaskEntity task = new TaskEntity();
        task.setDescription(taskCreateDTO.getDescription());
        // Convert LocalDate to LocalDateTime with time set to 11:59:59 PM
        task.setDueDate(taskCreateDTO.getDueDate().atTime(23, 59, 59));
        task.setTaskStatus(TaskStatus.TO_DO); // Automatically set to TO_DO for new tasks
        task.setTaskDifficulty(taskCreateDTO.getTaskDifficulty());
        task.setTaskCategory(taskCreateDTO.getTaskCategory());
        task.setTaskRewardPoints(10);
        return task;
    }

    // Converts a TaskUpdateDTO to a TaskEntity
    public TaskEntity toTaskEntity(TaskUpdateDTO taskUpdateDTO) {
        return modelMapper.map(taskUpdateDTO, TaskEntity.class);
    }

    // Converts a list of TaskEntity to a list of TaskDTO (uses manual mapping)
    public List<TaskDTO> toTaskDTOList(List<TaskEntity> tasks) {
        return tasks.stream()
                .map(this::toTaskDTO)
                .collect(Collectors.toList());
    }
}