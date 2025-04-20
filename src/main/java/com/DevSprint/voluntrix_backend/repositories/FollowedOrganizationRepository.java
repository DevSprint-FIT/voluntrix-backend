package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.FollowedOrganization;
import com.DevSprint.voluntrix_backend.entities.FollowedOrganizationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowedOrganizationRepository extends JpaRepository<FollowedOrganization, FollowedOrganizationId> {
    List<FollowedOrganization> findByVolunteerId(Long volunteerId);
    void deleteByVolunteerIdAndOrganizationId(Long volunteerId, Long organizationId);


    @Query("SELECT MONTH(f.followedAt), COUNT(f) FROM FollowedOrganization f " +
            "WHERE YEAR(f.followedAt) = :year AND f.organizationId = :orgId " +
            "GROUP BY MONTH(f.followedAt) ORDER BY MONTH(f.followedAt)")
    List<Object[]> countMonthlyFollowers(@Param("year") int year, @Param("orgId") Long orgId);
}
