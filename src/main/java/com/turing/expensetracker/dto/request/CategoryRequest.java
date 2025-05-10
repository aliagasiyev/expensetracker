package com.turing.expensetracker.dto.request;

import com.turing.expensetracker.enums.CategoryType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Category type cannot be null")
    private CategoryType type;

    private String color;
}