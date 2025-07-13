package com.DevSprint.voluntrix_backend.utils;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.SignupRequestDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.AuthProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {
    
    private final PasswordEncoder passwordEncoder;

    public UserEntity toEntity(SignupRequestDTO dto) {
        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAuthProvider(AuthProvider.EMAIL);

        return user;
    }

    public UserDetails toUserDetails(UserEntity user) {
        String authority = user.getRole() != null 
            ? "ROLE_" + user.getRole().name()
            : "ROLE_UNASSIGNED";
            
        return new User(
            user.getUsername(), // Use actual username instead of email
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority(authority))
        );
    }
}
