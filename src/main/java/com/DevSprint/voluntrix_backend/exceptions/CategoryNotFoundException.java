package com.DevSprint.voluntrix_backend.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String string) {
        super(string);
    }
}
