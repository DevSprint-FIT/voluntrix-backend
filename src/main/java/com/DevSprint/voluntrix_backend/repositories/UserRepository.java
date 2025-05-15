package com.DevSprint.voluntrix_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DevSprint.voluntrix_backend.entities.ChatUser;

public interface UserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByUsername(String username);
}


