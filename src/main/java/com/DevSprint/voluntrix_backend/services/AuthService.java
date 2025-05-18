package com.DevSprint.voluntrix_backend.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.SignupRequestDto;
import com.DevSprint.voluntrix_backend.dtos.SignupResponseDto;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.utils.UserMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public ApiResponse<SignupResponseDto> signUp(SignupRequestDto request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        UserEntity user = userMapper.toEntity(request);
        userRepository.save(user);

        UserDetails userDetails = userMapper.toUserDetails(user);
        String token = jwtService.generateToken(userDetails);

        ApiResponse<SignupResponseDto> response = new ApiResponse<SignupResponseDto>(
            "User registered successfully",
            new SignupResponseDto(
                token,
                user.getRole().name(),
                user.getIsProfileCompleted()
            )
        );

        return response;
    }
    
}
