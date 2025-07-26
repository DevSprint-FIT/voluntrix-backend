package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO implements Serializable {

    private Long categoryId;
    private String categoryName;
}
