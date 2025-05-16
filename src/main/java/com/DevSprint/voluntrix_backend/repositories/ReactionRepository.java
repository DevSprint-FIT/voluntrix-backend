package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.ReactionEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<ReactionEntity, Long> {
    List<ReactionEntity> findBySocialFeedId(Long socialFeedId);
    List<ReactionEntity> findByUserIdAndUserType(Long userId, UserType userType);
    Optional<ReactionEntity> findBySocialFeedIdAndUserIdAndUserType(Long socialFeedId, Long userId, UserType userType);

}
