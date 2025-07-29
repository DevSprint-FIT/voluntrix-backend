package com.DevSprint.voluntrix_backend.repositories;

import com.DevSprint.voluntrix_backend.entities.PrivateRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivateRoomRepository extends JpaRepository<PrivateRoom, Long> {
    
    Optional<PrivateRoom> findByRoomId(String roomId);
    
    @Query("SELECT pr FROM PrivateRoom pr WHERE pr.user1 = :username OR pr.user2 = :username")
    List<PrivateRoom> findRoomsByUser(@Param("username") String username);
    
    @Query("SELECT pr FROM PrivateRoom pr WHERE (pr.user1 = :user1 AND pr.user2 = :user2) OR (pr.user1 = :user2 AND pr.user2 = :user1)")
    Optional<PrivateRoom> findRoomByUsers(@Param("user1") String user1, @Param("user2") String user2);
}
