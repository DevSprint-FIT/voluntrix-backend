package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.TaskEntity;
import com.DevSprint.voluntrix_backend.enums.TaskStatus;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    
    // Custom findById to load assignee user data
    @Query("SELECT t FROM TaskEntity t " +
           "LEFT JOIN FETCH t.assignee a " +
           "LEFT JOIN FETCH a.user " +
           "WHERE t.taskId = :taskId")
    Optional<TaskEntity> findByIdWithAssigneeUser(@Param("taskId") Long taskId);
    
    @Query("SELECT t FROM TaskEntity t " +
           "LEFT JOIN FETCH t.assignee a " +
           "LEFT JOIN FETCH a.user " +
           "WHERE t.assignee.volunteerId = :assigneeId")
    List<TaskEntity> findByAssignee_VolunteerId(@Param("assigneeId") Long assigneeId);
    
    @Query("SELECT t FROM TaskEntity t " +
           "LEFT JOIN FETCH t.assignee a " +
           "LEFT JOIN FETCH a.user " +
           "WHERE t.event.eventId = :eventId")
    List<TaskEntity> findByEvent_EventId(@Param("eventId") Long eventId);
    
    @Query("SELECT t FROM TaskEntity t " +
           "LEFT JOIN FETCH t.assignee a " +
           "LEFT JOIN FETCH a.user " +
           "WHERE t.assignee.volunteerId = :assigneeId AND t.event.eventId = :eventId")
    List<TaskEntity> findByAssignee_VolunteerIdAndEvent_EventId(@Param("assigneeId") Long assigneeId, @Param("eventId") Long eventId);
    
    // New methods for task status filtering with JOIN FETCH to load assignee user data
    @Query("SELECT t FROM TaskEntity t " +
           "LEFT JOIN FETCH t.assignee a " +
           "LEFT JOIN FETCH a.user " +
           "WHERE t.event.eventId = :eventId AND t.taskStatus = :taskStatus")
    List<TaskEntity> findByEvent_EventIdAndTaskStatus(@Param("eventId") Long eventId, @Param("taskStatus") TaskStatus taskStatus);
    
    @Query("SELECT t FROM TaskEntity t " +
           "LEFT JOIN FETCH t.assignee a " +
           "LEFT JOIN FETCH a.user " +
           "WHERE t.assignee.volunteerId = :assigneeId AND t.event.eventId = :eventId AND t.taskStatus = :taskStatus")
    List<TaskEntity> findByAssignee_VolunteerIdAndEvent_EventIdAndTaskStatus(@Param("assigneeId") Long assigneeId, @Param("eventId") Long eventId, @Param("taskStatus") TaskStatus taskStatus);
    
    // New methods for task status counting
    Long countByEvent_EventIdAndTaskStatus(Long eventId, TaskStatus taskStatus);
    Long countByAssignee_VolunteerIdAndEvent_EventIdAndTaskStatus(Long assigneeId, Long eventId, TaskStatus taskStatus);

    @Query("SELECT t FROM TaskEntity t " +
           "LEFT JOIN FETCH t.assignee v " +
           "LEFT JOIN FETCH v.user " +
           "JOIN VolunteerEventParticipationEntity p ON p.volunteer.volunteerId = v.volunteerId AND p.event.eventId = t.event.eventId " +
           "WHERE t.assignee IS NOT NULL " +
           "AND t.createdDate <= :twoDaysAgo " +
           "AND t.taskStatus = 'TO_DO' " +
           "AND v.isAvailable = false " +
           "AND (p.isInactive IS NULL OR p.isInactive = false)")
    List<TaskEntity> findTasksAssignedMoreThanTwoDaysAgoWithTodoStatus(@Param("twoDaysAgo") LocalDateTime twoDaysAgo);

    // Query to get tasks with submitted dates for a specific assignee
    @Query("SELECT t FROM TaskEntity t " +
           "LEFT JOIN FETCH t.assignee a " +
           "LEFT JOIN FETCH a.user " +
           "WHERE t.assignee.volunteerId = :assigneeId " +
           "AND t.taskSubmittedDate IS NOT NULL " +
           "ORDER BY t.taskSubmittedDate")
    List<TaskEntity> findTasksWithSubmittedDatesByAssigneeId(@Param("assigneeId") Long assigneeId);
}
