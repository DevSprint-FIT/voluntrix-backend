package com.DevSprint.voluntrix_backend.dtos;

import java.io.Serializable;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {

    private Long categoryId;
    private String categoryName;
}
