package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.ContributionArea;
import com.DevSprint.voluntrix_backend.enums.TaskDifficulty;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDTO {
    @NotBlank(message = "Task description must not be blank")
    @Size(max = 500, message = "Description can be up to 500 characters")
    private String description;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull(message = "Task difficulty is required")
    private TaskDifficulty taskDifficulty; // EASY, MEDIUM, HARD, EXTREME

    @NotNull(message = "Task category is required")
    private ContributionArea taskCategory; // DESIGN, EDITORIAL, LOGISTICS, PROGRAM

    @NotNull(message = "Assignee ID is required")
    private Long assigneeId;

    @NotNull(message = "Event ID is required")
    private Long eventId;
}
