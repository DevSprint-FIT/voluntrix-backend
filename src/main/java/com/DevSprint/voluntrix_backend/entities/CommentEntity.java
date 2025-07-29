package com.DevSprint.voluntrix_backend.entities;

import com.DevSprint.voluntrix_backend.enums.UserType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "social_feed_id", nullable = false)
    private SocialFeedEntity socialFeed;

    @Column(nullable = false)
    private String userUsername;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType; // VOLUNTEER, ORGANIZATION, etc.

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
