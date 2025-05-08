package com.turing.expensetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ExpenseCategoryRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String name;

    @Size(max = 200, message = "Description cannot be longer than 200 characters")
    private String description;
}
