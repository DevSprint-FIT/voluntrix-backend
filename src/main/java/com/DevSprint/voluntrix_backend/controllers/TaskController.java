package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.TaskCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.TaskDTO;
import com.DevSprint.voluntrix_backend.dtos.TaskUpdateDTO;
import com.DevSprint.voluntrix_backend.enums.TaskStatus;
import com.DevSprint.voluntrix_backend.services.TaskService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.DevSprint.voluntrix_backend.enums.UserType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;
    private final CurrentUserService currentUserService;

    @RequiresRole(UserType.VOLUNTEER)
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        TaskDTO createdTask = taskService.createTask(taskCreateDTO);
        return ResponseEntity.status(201).body(createdTask);
    }

    @RequiresRole(UserType.VOLUNTEER)
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        TaskDTO task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }
    
    
    // New endpoint: Get tasks by event ID and task status
    @RequiresRole(UserType.VOLUNTEER)
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TaskDTO>> getTasksByEventIdAndStatus(
            @PathVariable Long eventId,
            @RequestParam TaskStatus taskStatus) {
        List<TaskDTO> tasks = taskService.getTasksByEventIdAndStatus(eventId, taskStatus);
        return ResponseEntity.ok(tasks);
    }

    // New endpoint: Get tasks by assignee ID, event ID and task status
    @RequiresRole(UserType.VOLUNTEER)
    @GetMapping("/assignee/event/{eventId}")
    public ResponseEntity<List<TaskDTO>> getTasksByAssigneeIdAndEventIdAndStatus(
            @PathVariable Long eventId,
            @RequestParam TaskStatus taskStatus) {
        Long assigneeId = currentUserService.getCurrentEntityId();
        List<TaskDTO> tasks = taskService.getTasksByAssigneeIdAndEventIdAndStatus(assigneeId, eventId, taskStatus);
        return ResponseEntity.ok(tasks);
    }

    // New endpoint: Get task status counts for an event
    @RequiresRole(UserType.VOLUNTEER)
    @GetMapping("/event/{eventId}/status-count")
    public ResponseEntity<Map<String, Long>> getTaskStatusCountsByEventId(@PathVariable Long eventId) {
        Map<String, Long> statusCounts = taskService.getTaskStatusCountsByEventId(eventId);
        return ResponseEntity.ok(statusCounts);
    }
    
    // New endpoint: Get task status counts for a volunteer and event
    @RequiresRole(UserType.VOLUNTEER)
    @GetMapping("/assignee/event/{eventId}/status-count")
    public ResponseEntity<Map<String, Long>> getTaskStatusCountsByAssigneeIdAndEventId(@PathVariable Long eventId) {
        Long assigneeId = currentUserService.getCurrentEntityId();
        Map<String, Long> statusCounts = taskService.getTaskStatusCountsByAssigneeIdAndEventId(assigneeId, eventId);
        return ResponseEntity.ok(statusCounts);
    }   


    @RequiresRole(UserType.VOLUNTEER)
    @GetMapping("/assignee/submitted-dates")
    public ResponseEntity<List<LocalDate>> getTaskSubmittedDatesByAssigneeId() {
        Long assigneeId = currentUserService.getCurrentEntityId();
        List<LocalDate> submittedDates = taskService.getTaskSubmittedDatesByAssigneeId(assigneeId);
        return ResponseEntity.ok(submittedDates);
    }

    @RequiresRole(UserType.VOLUNTEER)
    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskDTO> patchTask(@PathVariable Long taskId, @Valid @RequestBody TaskUpdateDTO taskUpdateDTO) {
        TaskDTO updatedTask = taskService.patchTask(taskId, taskUpdateDTO);
        return ResponseEntity.ok(updatedTask);
    }
    
    @RequiresRole(UserType.VOLUNTEER)
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @RequiresRole(UserType.VOLUNTEER)
    @GetMapping("/volunteer/rewards")
    public ResponseEntity<Map<String, Object>> getVolunteerRewardStats() {
        Long volunteerId = currentUserService.getCurrentEntityId();
        Map<String, Object> rewardStats = taskService.getVolunteerRewardStats(volunteerId);
        return ResponseEntity.ok(rewardStats);
    }
}

