package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
