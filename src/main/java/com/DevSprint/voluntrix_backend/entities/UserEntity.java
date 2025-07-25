package com.DevSprint.voluntrix_backend.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.DevSprint.voluntrix_backend.enums.AuthProvider;
import com.DevSprint.voluntrix_backend.enums.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")  // user is a reserved keyword in SQL, so we use users
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String handle; // User's display handle/username

    @Column(nullable = false)
    private String fullName;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthProvider authProvider;

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean isVerified = false;

    @Enumerated(EnumType.STRING)
    private UserType role;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isProfileCompleted = false;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return List.of(new SimpleGrantedAuthority("ROLE_UNASSIGNED"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email; // Spring Security requires this method name, but we use email as username
    }

    // Getter for the display handle
    public String getHandle() {
        return this.handle;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return true; 
    }
}
