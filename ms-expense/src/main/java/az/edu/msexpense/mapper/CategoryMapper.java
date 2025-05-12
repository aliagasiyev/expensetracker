package az.edu.msexpense.mapper;

import az.edu.msexpense.dto.request.CategoryRequest;
import az.edu.msexpense.dto.response.CategoryResponse;
import az.edu.msexpense.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category entity);
}