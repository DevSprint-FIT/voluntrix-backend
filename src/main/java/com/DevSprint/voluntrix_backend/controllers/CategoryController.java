package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.*;
import com.DevSprint.voluntrix_backend.services.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{categoryId}/followers")
    public ResponseEntity<Set<VolunteerCategoryDTO>> getCategoryFollowers(@PathVariable Long categoryId) {
        Set<VolunteerCategoryDTO> followers = categoryService.getCategoryFollowers(categoryId);
        return ResponseEntity.ok(followers);
    }
}
