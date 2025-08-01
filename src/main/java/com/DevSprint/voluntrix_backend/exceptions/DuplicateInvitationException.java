package com.DevSprint.voluntrix_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DuplicateInvitationException extends RuntimeException {

    public DuplicateInvitationException(String message) {
        super(message);
    }
}
