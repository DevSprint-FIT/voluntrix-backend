package com.DevSprint.voluntrix_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.DevSprint.voluntrix_backend.entities.InstituteEntity;

@Repository
public interface InstituteRepository extends JpaRepository<InstituteEntity, String> {
    
    Optional<InstituteEntity> findByKey(String key);
    
    Optional<InstituteEntity> findByName(String name);
    
    Optional<InstituteEntity> findByDomain(String domain);
    
    @Query("SELECT i FROM InstituteEntity i ORDER BY i.name ASC")
    List<InstituteEntity> findAllOrderedByName();
    
    @Query("SELECT i FROM InstituteEntity i WHERE i.key = :key")
    Optional<InstituteEntity> findByKeyIgnoreCase(@Param("key") String key);
}
