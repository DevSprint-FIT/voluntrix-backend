package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.UserSponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSponsorRepository extends JpaRepository<UserSponsor, Long> {
    
    Optional<UserSponsor> findByUsername(String username);
    
    Optional<UserSponsor> findByEmail(String email);
    
    List<UserSponsor> findByIsActiveTrue();
    
    @Query("SELECT us FROM UserSponsor us WHERE us.isActive = true ORDER BY us.isOnline DESC, us.username ASC")
    List<UserSponsor> findAllActiveSponsorsOrderedByOnlineStatus();
    
    @Query("SELECT us FROM UserSponsor us WHERE us.isActive = true AND (us.username LIKE %:searchTerm% OR us.firstName LIKE %:searchTerm% OR us.lastName LIKE %:searchTerm% OR us.companyName LIKE %:searchTerm%)")
    List<UserSponsor> searchActiveSponsors(String searchTerm);
}
