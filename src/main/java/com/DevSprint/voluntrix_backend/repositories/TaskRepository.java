package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.TaskEntity;
import com.DevSprint.voluntrix_backend.enums.TaskStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByAssignee_VolunteerId(Long assigneeId);
    List<TaskEntity> findByEvent_EventId(Long eventId);
    List<TaskEntity> findByAssignee_VolunteerIdAndEvent_EventId(Long assigneeId, Long eventId);
    
    // New methods for task status filtering
    List<TaskEntity> findByEvent_EventIdAndTaskStatus(Long eventId, TaskStatus taskStatus);
    List<TaskEntity> findByAssignee_VolunteerIdAndEvent_EventIdAndTaskStatus(Long assigneeId, Long eventId, TaskStatus taskStatus);
    
    // New methods for task status counting
    Long countByEvent_EventIdAndTaskStatus(Long eventId, TaskStatus taskStatus);
    Long countByAssignee_VolunteerIdAndEvent_EventIdAndTaskStatus(Long assigneeId, Long eventId, TaskStatus taskStatus);

    @Query("SELECT t FROM TaskEntity t " +
            "JOIN t.assignee v " +
            "JOIN VolunteerEventParticipationEntity p ON p.volunteer.volunteerId = v.volunteerId AND p.event.eventId = t.event.eventId " +
            "WHERE t.assignee IS NOT NULL " +
            "AND t.createdDate <= :twoDaysAgo " +
            "AND t.taskStatus = 'TO_DO' " +
            "AND v.isAvailable = false " +
            "AND (p.isInactive IS NULL OR p.isInactive = false)")
    List<TaskEntity> findTasksAssignedMoreThanTwoDaysAgoWithTodoStatus(@Param("twoDaysAgo") LocalDateTime twoDaysAgo);

    // Query to get tasks with submitted dates for a specific assignee
    @Query("SELECT t FROM TaskEntity t " +
            "WHERE t.assignee.volunteerId = :assigneeId " +
            "AND t.taskSubmittedDate IS NOT NULL " +
            "ORDER BY t.taskSubmittedDate")
    List<TaskEntity> findTasksWithSubmittedDatesByAssigneeId(@Param("assigneeId") Long assigneeId);

}