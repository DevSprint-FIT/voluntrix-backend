package com.DevSprint.voluntrix_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevSprint.voluntrix_backend.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByHandle(String handle);
    boolean existsByEmail(String email);
    boolean existsByHandle(String handle);
}
