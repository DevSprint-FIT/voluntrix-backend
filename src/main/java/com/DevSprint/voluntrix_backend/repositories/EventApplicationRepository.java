package com.DevSprint.voluntrix_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.DevSprint.voluntrix_backend.entities.EventApplicationEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;

public interface EventApplicationRepository extends JpaRepository<EventApplicationEntity, Long> {

    boolean existsByEventAndVolunteer(EventEntity event, VolunteerEntity volunteer);

    @Query("SELECT ea FROM EventApplicationEntity ea WHERE ea.event = :event AND ea.volunteer = :volunteer")
    Optional<EventApplicationEntity> findByEventAndVolunteer(@Param("event") EventEntity event,
            @Param("volunteer") VolunteerEntity volunteer);

}
