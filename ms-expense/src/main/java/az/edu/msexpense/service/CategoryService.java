package az.edu.msexpense.service;

import az.edu.msexpense.dto.request.CategoryRequest;
import az.edu.msexpense.dto.response.CategoryResponse;
import az.edu.msexpense.enums.CategoryType;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getCategoriesByType(CategoryType type);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest request);
    
    void deleteCategory(Long id);
}