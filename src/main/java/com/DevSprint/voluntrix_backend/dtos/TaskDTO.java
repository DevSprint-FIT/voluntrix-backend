package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.ContributionArea;
import com.DevSprint.voluntrix_backend.enums.TaskStatus;
import com.DevSprint.voluntrix_backend.enums.TaskDifficulty;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long taskId;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updatedDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    
    private TaskStatus taskStatus; // TO_DO, IN_PROGRESS, DONE
    private TaskDifficulty taskDifficulty; // EASY, MEDIUM, HARD, EXTREME
    private ContributionArea taskCategory; // DESIGN, EDITORIAL, LOGISTICS, PROGRAMMING
    private String resourceUrl;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate taskSubmittedDate;

    private Long assigneeId;
    private String assigneeUsername;

    private Long eventId;
    private String eventTitle;

    private Integer taskRewardPoints;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate taskReviewedDate;
}
