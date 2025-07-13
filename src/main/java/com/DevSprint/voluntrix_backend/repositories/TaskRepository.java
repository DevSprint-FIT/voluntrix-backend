package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.TaskEntity;
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

    @Query("SELECT t FROM TaskEntity t " +
            "JOIN t.assignee v " +
            "JOIN VolunteerEventParticipationEntity p ON p.volunteer.volunteerId = v.volunteerId AND p.event.eventId = t.event.eventId " +
            "WHERE t.assignee IS NOT NULL " +
            "AND t.createdDate <= :twoDaysAgo " +
            "AND t.taskStatus = 'TO_DO' " +
            "AND v.isAvailable = false " +
            "AND (p.isInactive IS NULL OR p.isInactive = false)")
    List<TaskEntity> findTasksAssignedMoreThanTwoDaysAgoWithTodoStatus(@Param("twoDaysAgo") LocalDateTime twoDaysAgo);

}