package com.DevSprint.voluntrix_backend.utils;

import com.DevSprint.voluntrix_backend.dtos.CategoryDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCategoryDTO;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VolunteerCategoryEntityDTOConverter {


    //  method to convert Volunteer's categories to CategoryDTOs
    public Set<CategoryDTO> convertVolunteerCategoriesToCategoryDTOs(VolunteerEntity volunteer) {
        return volunteer.getFollowedCategories().stream()
                .map(category -> CategoryDTO.builder()
                        .categoryId(category.getCategoryId())
                        .categoryName(category.getCategoryName())
                        .build())
                .collect(Collectors.toSet());
    }

    
    public Set<VolunteerCategoryDTO> convertVolunteerCategoriesToDTOs(VolunteerEntity volunteer) {
        return volunteer.getFollowedCategories().stream()
                .map(category -> VolunteerCategoryDTO.builder()
                        .volunteerId(volunteer.getVolunteerId())
                        .categoryId(category.getCategoryId())
                        .build())
                .collect(Collectors.toSet());
    }

    public Set<VolunteerCategoryDTO> convertCategoryFollowersToDTOs(CategoryEntity category) {
        return category.getFollowers().stream()
                .map(volunteer -> VolunteerCategoryDTO.builder()
                        .volunteerId(volunteer.getVolunteerId())
                        .categoryId(category.getCategoryId())
                        .build())
                .collect(Collectors.toSet());
    }

    public VolunteerCategoryDTO convertToDTO(VolunteerEntity volunteer, CategoryEntity category) {
        return VolunteerCategoryDTO.builder()
                .volunteerId(volunteer.getVolunteerId())
                .categoryId(category.getCategoryId())
                .build();
    }
}
