package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.*;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.*;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/public/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @RequiresRole({UserType.VOLUNTEER, UserType.ORGANIZATION, UserType.ADMIN})
    @GetMapping("/{categoryId}/followers")
    public ResponseEntity<Set<VolunteerCategoryDTO>> getCategoryFollowers(@PathVariable Long categoryId) {
        Set<VolunteerCategoryDTO> followers = categoryService.getCategoryFollowers(categoryId);
        return ResponseEntity.ok(followers);
    }

    @RequiresRole(UserType.ADMIN)
    @PostMapping("/")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryCreateDTO categoryCreateDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }
}
