package com.DevSprint.voluntrix_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DevSprint.voluntrix_backend.entities.EventApplicationEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;

public interface EventApplicationRepository extends JpaRepository<EventApplicationEntity, Long> {

    boolean existsByEventAndVolunteer(EventEntity event, VolunteerEntity volunteer);

}