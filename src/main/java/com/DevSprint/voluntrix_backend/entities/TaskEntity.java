package com.DevSprint.voluntrix_backend.entities;

import com.DevSprint.voluntrix_backend.enums.TaskDifficulty;
import com.DevSprint.voluntrix_backend.enums.TaskStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false, length = 500)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus taskStatus; // TO_DO, IN_PROGRESS, DONE

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskDifficulty taskDifficulty; // EASY, MEDIUM, HARD, EXTREME

    @Column(length = 1000)
    private String resourceUrl;

    @Column
    private LocalDateTime taskSubmittedDate;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private VolunteerEntity assignee;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;
}
