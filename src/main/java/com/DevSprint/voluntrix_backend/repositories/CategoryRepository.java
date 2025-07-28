package com.DevSprint.voluntrix_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.Repository;

import com.DevSprint.voluntrix_backend.entities.CategoryEntity;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.followers WHERE c.categoryId = :categoryId")
    Optional<CategoryEntity> findByIdWithFollowers(@Param("categoryId") Long categoryId);
}
