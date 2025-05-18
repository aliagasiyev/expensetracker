package az.edu.msanalytics.model.dto.request;

import az.edu.msanalytics.enums.CategoryType;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {


    private Long id;
    @NotBlank(message = "Category name cannot be blank.")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters.")
    private String name;

    @NotNull(message = "Category type cannot be null.")
    private CategoryType type;

    @Pattern(regexp = "^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Color must be a valid hex code (e.g., #FF5733 or #F57).")
    @NotBlank(message = "Color cannot be blank.")
    private String color;
}