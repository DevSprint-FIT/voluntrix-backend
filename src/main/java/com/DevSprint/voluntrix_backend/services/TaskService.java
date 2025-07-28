package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.TaskCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.TaskDTO;
import com.DevSprint.voluntrix_backend.dtos.TaskUpdateDTO;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.TaskEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.enums.TaskDifficulty;
import com.DevSprint.voluntrix_backend.enums.TaskStatus;
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
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final VolunteerRepository volunteerRepository;
    private final EventRepository eventRepository;
    private final TaskDTOConvert taskDTOConvert;
    private final RewardService rewardService;

    public TaskService(TaskRepository taskRepository, VolunteerRepository volunteerRepository, EventRepository eventRepository, TaskDTOConvert taskDTOConvert, RewardService rewardService) {
        this.taskRepository = taskRepository;
        this.taskDTOConvert = taskDTOConvert;
        this.volunteerRepository = volunteerRepository;
        this.eventRepository = eventRepository;
        this.rewardService = rewardService;
    }

    public TaskDTO createTask(TaskCreateDTO taskCreateDTO) {
        TaskEntity task = taskDTOConvert.toTaskEntity(taskCreateDTO);

        // Set the related Event entity
        EventEntity event = eventRepository.findById(taskCreateDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + taskCreateDTO.getEventId()));
        task.setEvent(event);

        // System automatically assigns 10 points as base reward
        task.setTaskRewardPoints(10);

        // Set the related Volunteer (assignee) entity if present
        if (taskCreateDTO.getAssigneeId() != null) {
            VolunteerEntity assignee = volunteerRepository.findById(taskCreateDTO.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with ID: " + taskCreateDTO.getAssigneeId()));
            task.setAssignee(assignee);
        }

        TaskEntity savedTask = taskRepository.save(task);

        if (savedTask.getEvent().getEventHost() != null) {
            rewardService.processTaskCreation(savedTask);
        }

        // Give 10 points to assigned volunteer
        if (savedTask.getAssignee() != null) {
            rewardService.processTaskAssignment(savedTask);
        }
        return taskDTOConvert.toTaskDTO(savedTask);
    }


    public TaskDTO getTaskById(Long taskId) {
        Optional<TaskEntity> task = taskRepository.findById(taskId);
        return task.map(taskDTOConvert::toTaskDTO)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
    }

    public List<TaskDTO> getTasksByEventId(Long eventId) {
        List<TaskEntity> tasks = taskRepository.findByEvent_EventId(eventId);
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("No tasks found for event ID: " + eventId);
        }
        return taskDTOConvert.toTaskDTOList(tasks);
    }

    public List<TaskDTO> getTasksByAssigneeIdAndEventId(Long assigneeId, Long eventId) {
        List<TaskEntity> tasks = taskRepository.findByAssignee_VolunteerIdAndEvent_EventId(assigneeId, eventId);
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("No tasks found for assignee ID: " + assigneeId + " and event ID: " + eventId);
        }
        return taskDTOConvert.toTaskDTOList(tasks);
    }

    // New methods for filtered task retrieval
    public List<TaskDTO> getTasksByEventIdAndStatus(Long eventId, TaskStatus taskStatus) {
        List<TaskEntity> tasks = taskRepository.findByEvent_EventIdAndTaskStatus(eventId, taskStatus);
        return taskDTOConvert.toTaskDTOList(tasks);
    }

    public List<TaskDTO> getTasksByAssigneeIdAndEventIdAndStatus(Long assigneeId, Long eventId, TaskStatus taskStatus) {
        List<TaskEntity> tasks = taskRepository.findByAssignee_VolunteerIdAndEvent_EventIdAndTaskStatus(assigneeId, eventId, taskStatus);
        return taskDTOConvert.toTaskDTOList(tasks);
    }

    // New methods for task status counts
    public Map<String, Long> getTaskStatusCountsByEventId(Long eventId) {
        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("TO_DO", taskRepository.countByEvent_EventIdAndTaskStatus(eventId, TaskStatus.TO_DO));
        statusCounts.put("IN_PROGRESS", taskRepository.countByEvent_EventIdAndTaskStatus(eventId, TaskStatus.IN_PROGRESS));
        statusCounts.put("DONE", taskRepository.countByEvent_EventIdAndTaskStatus(eventId, TaskStatus.DONE));
        return statusCounts;
    }

    public Map<String, Long> getTaskStatusCountsByAssigneeIdAndEventId(Long assigneeId, Long eventId) {
        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("TO_DO", taskRepository.countByAssignee_VolunteerIdAndEvent_EventIdAndTaskStatus(assigneeId, eventId, TaskStatus.TO_DO));
        statusCounts.put("IN_PROGRESS", taskRepository.countByAssignee_VolunteerIdAndEvent_EventIdAndTaskStatus(assigneeId, eventId, TaskStatus.IN_PROGRESS));
        statusCounts.put("DONE", taskRepository.countByAssignee_VolunteerIdAndEvent_EventIdAndTaskStatus(assigneeId, eventId, TaskStatus.DONE));
        return statusCounts;
    }

    public List<LocalDate> getTaskSubmittedDatesByAssigneeId(Long assigneeId) {
        List<TaskEntity> tasks = taskRepository.findTasksWithSubmittedDatesByAssigneeId(assigneeId);
        return tasks.stream()
                .map(task -> task.getTaskSubmittedDate().toLocalDate())
                .sorted()
                .collect(Collectors.toList());
    }

    public TaskDTO patchTask(Long taskId, TaskUpdateDTO taskUpdateDTO) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));

        // Track old values for comparison
        TaskStatus oldStatus = task.getTaskStatus();
        Long oldAssigneeId = task.getAssignee() != null ? task.getAssignee().getVolunteerId() : null;

        // Update only the fields provided in the DTO
        if (taskUpdateDTO.getDescription() != null) {
            task.setDescription(taskUpdateDTO.getDescription());
        }
        if (taskUpdateDTO.getDueDate() != null) {
            // Convert LocalDate to LocalDateTime with time set to 11:59:59 PM
            task.setDueDate(taskUpdateDTO.getDueDate().atTime(23, 59, 59));
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
        if (taskUpdateDTO.getTaskReviewedDate() != null) {
            task.setTaskReviewedDate(taskUpdateDTO.getTaskReviewedDate());
        }
        if (taskUpdateDTO.getAssigneeId() != null) {
            VolunteerEntity assignee = volunteerRepository.findById(taskUpdateDTO.getAssigneeId())
                    .orElseThrow(() -> new VolunteerNotFoundException("Assignee not found with ID: " + taskUpdateDTO.getAssigneeId()));
            task.setAssignee(assignee);
        }

        // Handle task submission (TO_DO -> IN_PROGRESS)
        if (oldStatus == TaskStatus.TO_DO && task.getTaskStatus() == TaskStatus.IN_PROGRESS) {
            // Only set to current time if no submitted date was provided in the DTO
            if (taskUpdateDTO.getTaskSubmittedDate() == null) {
                task.setTaskSubmittedDate(LocalDateTime.now());
            }
            rewardService.processTaskSubmission(task);
        }

        // Handle task review (IN_PROGRESS -> DONE or TO_DO)
        if (oldStatus == TaskStatus.IN_PROGRESS &&
                (task.getTaskStatus() == TaskStatus.DONE || task.getTaskStatus() == TaskStatus.TO_DO)) {

            // Set review date to current time if not provided in DTO
            if (taskUpdateDTO.getTaskReviewedDate() == null){
                task.setTaskReviewedDate(LocalDateTime.now());
            }
            rewardService.processTaskReview(task, oldStatus);
        }

        // Handle task reassignment
        if (oldAssigneeId != null && taskUpdateDTO.getAssigneeId() != null &&
                !oldAssigneeId.equals(taskUpdateDTO.getAssigneeId())) {
            rewardService.processTaskReassignment(task, oldAssigneeId);
        }

        TaskEntity updatedTask = taskRepository.save(task);
        return taskDTOConvert.toTaskDTO(updatedTask);
    }

    public void deleteTask(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));

        // Process reward deductions before deleting
        rewardService.processTaskDeletion(task);

        // Now delete the task
        taskRepository.deleteById(taskId);
    }

    public Map<String, Object> getVolunteerRewardStats(String username) {
        // Get volunteer by username
        VolunteerEntity volunteer = volunteerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Volunteer not found: " + username));

        // Determine volunteer level
        String level = determineVolunteerLevel(volunteer.getRewardPoints());

        // Build response map
        Map<String, Object> stats = new HashMap<>();

        stats.put("name", volunteer.getUser().getFullName());
        stats.put("totalRewardPoints", volunteer.getRewardPoints());
        stats.put("level", level);
        stats.put("profilePictureUrl", volunteer.getProfilePictureUrl());
        return stats;
    }

    @SuppressWarnings("unused")
    private int getDifficultyPoints(TaskDifficulty difficulty) {
        if (difficulty == null) return 0;
        switch (difficulty) {
            case EASY:
                return 2;
            case MEDIUM:
                return 4;
            case HARD:
                return 6;
            case EXTREME:
                return 8;
            default:
                return 0;
        }
    }

    private String determineVolunteerLevel(int rewardPoints) {
        if (rewardPoints >= 1000) return "3";
        if (rewardPoints >= 500) return "2";
        return "1";
    }
}