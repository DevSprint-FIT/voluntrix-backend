package com.DevSprint.voluntrix_backend.exceptions;

public class VolunteerAlreadyFollowsOrganizationException extends RuntimeException {
    public VolunteerAlreadyFollowsOrganizationException(String message) {
        super(message);
    }
}