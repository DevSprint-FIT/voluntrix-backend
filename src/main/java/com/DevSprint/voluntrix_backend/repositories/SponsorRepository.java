package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.SponsorEntity;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SponsorRepository extends JpaRepository<SponsorEntity, Long> {
    
    @Query("SELECT s FROM SponsorEntity s WHERE s.user.handle = :username")
    Optional<SponsorEntity> findByUsername(@Param("username") String username);
    
    Optional<SponsorEntity> findByUser(UserEntity user);
}
