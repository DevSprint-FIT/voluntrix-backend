package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.FollowedOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowedOrganizationRepository extends JpaRepository<FollowedOrganization, Long> {

   
    List<FollowedOrganization> findByVolunteerId(Long volunteerId);

    
    void deleteByVolunteerIdAndOrganizationId(Long volunteerId, Long organizationId);
}
