package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.dtos.OrganizationNameDTO;
import com.DevSprint.voluntrix_backend.entities.OrganizationEntity;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {
    
    @Query("SELECT o FROM OrganizationEntity o WHERE o.user.handle = :username")
    Optional<OrganizationEntity> findByUsername(@Param("username") String username);
    
    Optional<OrganizationEntity> findByUser(UserEntity user);
    Optional<OrganizationEntity> findByUsername(String username);

    // JPQL Query
    @Query("SELECT new com.DevSprint.voluntrix_backend.dtos.OrganizationNameDTO(e.id, e.name, e.imageUrl) FROM OrganizationEntity e")
    List<OrganizationNameDTO> findAllOrganizationIdNameAndUrl();
}
