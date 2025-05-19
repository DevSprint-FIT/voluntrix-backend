package com.DevSprint.voluntrix_backend.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.LoginRequestDTO;
import com.DevSprint.voluntrix_backend.dtos.SignupRequestDto;
import com.DevSprint.voluntrix_backend.dtos.SignupResponseDto;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.exceptions.UserNotFoundException;
import com.DevSprint.voluntrix_backend.repositories.OrganizationRepository;
import com.DevSprint.voluntrix_backend.repositories.SponsorRepository;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.utils.UserMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final VolunteerRepository volunteerRepository;
    private final SponsorRepository sponsorRepository;
    private final OrganizationRepository organizationRepository;

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

    public SignupResponseDto login(LoginRequestDTO request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        boolean isProfileCompleted = switch (user.getRole()) {
            case VOLUNTEER -> volunteerRepository.existsById(user.getUserId());
            case SPONSOR -> sponsorRepository.existsById(user.getUserId());
            case ORGANIZATION -> organizationRepository.existsById(user.getUserId());
            case PUBLIC -> true;
        };

        return new SignupResponseDto(
            token,
            user.getRole().name(),
            isProfileCompleted
        );
    }
    
}
