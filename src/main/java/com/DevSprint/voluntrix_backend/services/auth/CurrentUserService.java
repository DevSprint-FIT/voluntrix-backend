package com.DevSprint.voluntrix_backend.services.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CurrentUserService {
    
    private final UserRepository userRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User no found"))
                    .getUserId();
        }
        throw new RuntimeException("Unauthorized");
    }

}
