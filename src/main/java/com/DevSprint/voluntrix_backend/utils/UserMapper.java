package com.DevSprint.voluntrix_backend.utils;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.dtos.SignupRequestDto;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.AuthProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {
    
    private final PasswordEncoder passwordEncoder;

    public UserEntity toEntity(SignupRequestDto dto) {
        UserEntity user  = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setAuthProvider(AuthProvider.EMAIL);

        return user;
    }

    public UserDetails toUserDetails(UserEntity user) {
        return new User(
            user.getEmail(),
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
