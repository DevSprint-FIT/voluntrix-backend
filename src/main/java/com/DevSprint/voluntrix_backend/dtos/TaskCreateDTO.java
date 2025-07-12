package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;

    @NotNull(message = "Task status is required")
    private TaskStatus taskStatus; // TO_DO, IN_PROGRESS, DONE

    private Long assigneeId;

    @NotNull(message = "Event ID is required")
    private Long eventId;          
}
