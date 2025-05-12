package az.edu.msexpense.dto.response;

import az.edu.msexpense.enums.CategoryType;

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
