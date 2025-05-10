package com.turing.expensetracker.dto.response;

import com.turing.expensetracker.enums.CategoryType;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;
    private String name;
    private CategoryType type;
    private String color;
}
