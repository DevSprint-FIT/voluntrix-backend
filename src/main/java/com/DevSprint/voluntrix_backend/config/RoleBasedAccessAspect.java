package com.DevSprint.voluntrix_backend.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.DevSprint.voluntrix_backend.exceptions.AccessDeniedException;
import com.DevSprint.voluntrix_backend.services.auth.RoleBasedAccessService;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleBasedAccessAspect {

    private final RoleBasedAccessService roleBasedAccessService;

    @Around("@annotation(requiresRole)")
    public Object checkRoleAccess(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
        // Check if user has any of the required roles
        if (!roleBasedAccessService.hasAnyRole(requiresRole.value())) {
            throw new AccessDeniedException(requiresRole.message());
        }

        return joinPoint.proceed();
    }
}
