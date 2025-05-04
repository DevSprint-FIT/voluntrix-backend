package com.DevSprint.voluntrix_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.DevSprint.voluntrix_backend.entities.EventApplicationEntity;
import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;

public interface EventApplicationRepository extends JpaRepository<EventApplicationEntity, Long> {

    boolean existsByEventAndVolunteer(EventEntity event, VolunteerEntity volunteer);

    // Find the count of applied events by volunteer ID
    @Query("SELECT COUNT(ea) FROM EventApplicationEntity ea WHERE ea.volunteer.id = :volunteerId AND ea.applicationStatus = 'PENDING'")
    long countPendingApplicationsByVolunteerId(@Param("volunteerId") Long volunteerId);
}