package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.TaskStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDTO {
    @Size(max = 500, message = "Description can be up to 500 characters")
    private String description;

    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    private TaskStatus taskStatus; // TO_DO, IN_PROGRESS, DONE

    @Size(max = 1000, message = "Resource URL is too long")
    private String resourceUrl;

    private LocalDateTime taskSubmittedDate;

    private Long assigneeId;
}
