package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, Long> {
    // Optional<VolunteerEntity> findByUsername(String username);
    // Optional<VolunteerEntity> findByEmail(String email);
    Optional<VolunteerEntity> findByPhoneNumber(String phoneNumber);
    Optional<VolunteerEntity> findByVolunteerId(Long volunteerId);
    Optional<VolunteerEntity> findByUser(UserEntity user);
}
