package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.TaskCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.TaskDTO;
import com.DevSprint.voluntrix_backend.dtos.TaskUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.TaskEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.exceptions.ResourceNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.TaskNotFoundException;
import com.DevSprint.voluntrix_backend.exceptions.VolunteerNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.TaskRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;

import com.DevSprint.voluntrix_backend.utils.TaskDTOConvert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final VolunteerRepository volunteerRepository;
    private final EventRepository eventRepository;
    private final TaskDTOConvert taskDTOConvert;

    public TaskService(TaskRepository taskRepository, VolunteerRepository volunteerRepository, EventRepository eventRepository, TaskDTOConvert taskDTOConvert) {
        this.taskRepository = taskRepository;
        this.taskDTOConvert = taskDTOConvert;
        this.volunteerRepository = volunteerRepository;
        this.eventRepository = eventRepository;
    }

    public TaskDTO createTask(TaskCreateDTO taskCreateDTO) {
        TaskEntity task = taskDTOConvert.toTaskEntity(taskCreateDTO);

        // Set the related Event entity
        EventEntity event = eventRepository.findById(taskCreateDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + taskCreateDTO.getEventId()));
        task.setEvent(event);

        // Set the related Volunteer (assignee) entity if present
        if (taskCreateDTO.getAssigneeId() != null) {
            VolunteerEntity assignee = volunteerRepository.findById(taskCreateDTO.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with ID: " + taskCreateDTO.getAssigneeId()));
            task.setAssignee(assignee);
        }

        TaskEntity savedTask = taskRepository.save(task);
        return taskDTOConvert.toTaskDTO(savedTask);
    }


    public TaskDTO getTaskById(Long taskId) {
        Optional<TaskEntity> task = taskRepository.findById(taskId);
        return task.map(taskDTOConvert::toTaskDTO)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
    }

    public List<TaskDTO> getTasksByEventId(Long eventId) {
        return taskDTOConvert.toTaskDTOList(taskRepository.findByEvent_EventId(eventId));
    }

    public List<TaskDTO> getTasksByVolunteerUsername(String username) {
        return taskDTOConvert.toTaskDTOList(taskRepository.findByAssignee_Username(username));
    }

    public TaskDTO patchTask(Long taskId, TaskUpdateDTO taskUpdateDTO) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));

        // Update only the fields provided in the DTO
        if (taskUpdateDTO.getDescription() != null) {
            task.setDescription(taskUpdateDTO.getDescription());
        }
        if (taskUpdateDTO.getDueDate() != null) {
            task.setDueDate(taskUpdateDTO.getDueDate());
        }
        if (taskUpdateDTO.getTaskStatus() != null) {
            task.setTaskStatus(taskUpdateDTO.getTaskStatus());
        }
        if (taskUpdateDTO.getResourceUrl() != null) {
            task.setResourceUrl(taskUpdateDTO.getResourceUrl());
        }
        if (taskUpdateDTO.getTaskSubmittedDate() != null) {
            task.setTaskSubmittedDate(taskUpdateDTO.getTaskSubmittedDate());
        }
        if (taskUpdateDTO.getAssigneeId() != null) {
            VolunteerEntity assignee = volunteerRepository.findById(taskUpdateDTO.getAssigneeId())
                    .orElseThrow(() -> new VolunteerNotFoundException("Assignee not found with ID: " + taskUpdateDTO.getAssigneeId()));
            task.setAssignee(assignee);
        }

        TaskEntity updatedTask = taskRepository.save(task);
        return taskDTOConvert.toTaskDTO(updatedTask);
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found with ID: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }
}
