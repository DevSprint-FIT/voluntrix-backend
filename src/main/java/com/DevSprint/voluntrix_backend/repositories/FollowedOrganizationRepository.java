package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.dtos.*;
import com.DevSprint.voluntrix_backend.entities.FollowedOrganizationEntity;
import com.DevSprint.voluntrix_backend.entities.FollowedOrganizationIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowedOrganizationRepository extends JpaRepository<FollowedOrganizationEntity, FollowedOrganizationIdEntity> {
    List<FollowedOrganizationEntity> findByVolunteerId(Long volunteerId);
    void deleteByVolunteerIdAndOrganizationId(Long volunteerId, Long organizationId);


    @Query("SELECT MONTH(f.followedAt), COUNT(f) FROM FollowedOrganizationEntity f " +
            "WHERE YEAR(f.followedAt) = :year AND f.organizationId = :orgId " +
            "GROUP BY MONTH(f.followedAt) ORDER BY MONTH(f.followedAt)")
    List<Object[]> countMonthlyFollowers(@Param("year") int year, @Param("orgId") Long orgId);

    @Query("SELECT v.institute AS institute, COUNT(v) AS count " +
            "FROM FollowedOrganizationEntity f " +
            "JOIN VolunteerEntity v ON f.volunteerId = v.volunteerId " +
            "WHERE f.organizationId = :organizationId " +
            "GROUP BY v.institute")
    List<InstituteCountProjection> countVolunteersByInstitute(@Param("organizationId") Long organizationId);
}
