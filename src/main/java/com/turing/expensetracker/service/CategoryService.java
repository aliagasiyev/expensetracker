package com.turing.expensetracker.service;
import com.turing.expensetracker.dto.request.CategoryRequest;
import com.turing.expensetracker.dto.response.CategoryResponse;
import java.util.List;
import com.turing.expensetracker.enums.CategoryType;
public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getCategoriesByType(CategoryType type);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest request);
    
    void deleteCategory(Long id);
}