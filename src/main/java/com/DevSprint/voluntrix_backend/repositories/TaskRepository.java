package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByAssignee_VolunteerId(Long assigneeId);
    List<TaskEntity> findByEvent_EventId(Long eventId);
    List<TaskEntity> findByAssignee_Username(String username);
}
