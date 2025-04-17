package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "followed_organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FollowedOrganizationId.class)
public class FollowedOrganization {


    @Id
    @Column(nullable = false)
    private Long volunteerId;

    @Id
    @Column(nullable = false)
    private Long organizationId;


    /* Constructor with two arguments
    public FollowedOrganization(Long volunteerId, Long organizationId) {
        this.volunteerId = volunteerId;
        this.organizationId = organizationId;
    }

     */



}
