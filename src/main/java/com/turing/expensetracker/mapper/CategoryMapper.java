package com.turing.expensetracker.mapper;

import com.turing.expensetracker.dto.request.CategoryRequest;
import com.turing.expensetracker.dto.response.CategoryResponse;
import com.turing.expensetracker.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category entity);
}