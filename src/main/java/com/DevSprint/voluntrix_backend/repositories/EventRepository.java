package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import com.DevSprint.voluntrix_backend.entities.EventEntity;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByOrganizationIdAndEventStatus(Long organizationId, EventStatus eventStatus);
}