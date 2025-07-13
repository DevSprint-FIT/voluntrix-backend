package com.DevSprint.voluntrix_backend.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.AuthProvider;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import com.DevSprint.voluntrix_backend.services.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        Optional<UserEntity> exisitingUser = userRepository.findByEmail(email);
        UserEntity user;

        if(exisitingUser.isPresent()) {
            user = exisitingUser.get();
        } else {
            user = UserEntity.builder()
                    .email(email)
                    .authProvider(AuthProvider.GOOGLE)
                    .role(UserType.PUBLIC)
                    .isVerified(true)
                    .build();
            userRepository.save(user);
        }

        String jwtToken = jwtService.generateToken(user);
        
        String redirectUrl = "http://localhost:3000/auth/success?token=" + jwtToken + "&role=" + user.getRole();
        response.sendRedirect(redirectUrl); 
    }
}
