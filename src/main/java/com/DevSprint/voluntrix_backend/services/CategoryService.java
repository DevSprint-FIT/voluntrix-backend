package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.dtos.CategoryCreateDTO;
import com.DevSprint.voluntrix_backend.dtos.CategoryDTO;
import com.DevSprint.voluntrix_backend.dtos.VolunteerCategoryDTO;
import com.DevSprint.voluntrix_backend.entities.CategoryEntity;
import com.DevSprint.voluntrix_backend.repositories.CategoryRepository;
import com.DevSprint.voluntrix_backend.utils.VolunteerCategoryEntityDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final VolunteerCategoryEntityDTOConverter volunteerCategoryConverter;

    public Set<VolunteerCategoryDTO> getCategoryFollowers(Long categoryId) {
        CategoryEntity category = categoryRepository.findByIdWithFollowers(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        return volunteerCategoryConverter.convertCategoryFollowersToDTOs(category);
    }

    public CategoryDTO createCategory(CategoryCreateDTO categoryCreateDTO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryName(categoryCreateDTO.getCategoryName());
        
        // Save the new category to the repository
        CategoryEntity savedCategory = categoryRepository.save(categoryEntity);
        
        // Convert the saved entity to DTO
        return new CategoryDTO(savedCategory.getCategoryId(), savedCategory.getCategoryName());
    }
}
