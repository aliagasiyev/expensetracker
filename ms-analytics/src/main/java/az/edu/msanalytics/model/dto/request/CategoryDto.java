package az.edu.msanalytics.model.dto.request;

import az.edu.msanalytics.enums.CategoryType;
import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private CategoryType type;
    private String color;
}
