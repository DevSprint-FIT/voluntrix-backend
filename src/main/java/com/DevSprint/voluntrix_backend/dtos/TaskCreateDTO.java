package com.DevSprint.voluntrix_backend.dtos;

import com.DevSprint.voluntrix_backend.enums.TaskStatus;
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
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus taskStatus; // TO_DO, IN_PROGRESS, DONE    
    private Long assigneeId;       
    private Long eventId;          
}
