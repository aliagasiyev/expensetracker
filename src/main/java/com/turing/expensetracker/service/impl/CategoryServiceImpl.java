package com.turing.expensetracker.service.impl;

import com.turing.expensetracker.dto.request.CategoryRequest;
import com.turing.expensetracker.dto.response.CategoryResponse;
import com.turing.expensetracker.entity.Category;
import com.turing.expensetracker.enums.CategoryType;
import com.turing.expensetracker.exception.*;
import com.turing.expensetracker.mapper.CategoryMapper;
import com.turing.expensetracker.repository.CategoryRepository;
import com.turing.expensetracker.service.CategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        try {
            validateCategoryData(request);
            Category category = categoryMapper.toEntity(request);
            return categoryMapper.toResponse(categoryRepository.save(category));
        } catch (CategoryNotFoundException | InvalidCategoryDataException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCategoryDataException("Failed to create category: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                throw new ResourceNotFoundException("No categories found");
            }
            return categories.stream()
                    .map(categoryMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (CategoryNotFoundException | InvalidCategoryDataException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCategoryDataException("Failed to retrieve categories: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesByType(CategoryType type) {
        try {
            List<Category> categories = categoryRepository.findByType(type);
            if (categories.isEmpty()) {
                throw new ResourceNotFoundException("No categories found for type: " + type);
            }
            return categories.stream()
                    .map(categoryMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (CategoryNotFoundException | InvalidCategoryDataException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCategoryDataException("Failed to retrieve categories by type: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        try {
            return categoryRepository.findById(id)
                    .map(categoryMapper::toResponse)
                    .orElseThrow(() -> new CategoryNotFoundException(id));
        } catch (CategoryNotFoundException | InvalidCategoryDataException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCategoryDataException("Failed to retrieve category with id " + id + ": " + e.getMessage());
        }
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        try {
            validateCategoryData(request);
            Category existingCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new CategoryNotFoundException(id));
            
            Category updatedCategory = categoryMapper.toEntity(request);
            updatedCategory.setId(existingCategory.getId());
            
            return categoryMapper.toResponse(categoryRepository.save(updatedCategory));
        } catch (CategoryNotFoundException | InvalidCategoryDataException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCategoryDataException("Failed to update category: " + e.getMessage());
        }
    }

    @Override
    public void deleteCategory(Long id) {
        try {
            if (!categoryRepository.existsById(id)) {
                throw new CategoryNotFoundException(id);
            }
            categoryRepository.deleteById(id);
        } catch (CategoryNotFoundException | InvalidCategoryDataException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCategoryDataException("Failed to delete category: " + e.getMessage());
        }
    }

    private void validateCategoryData(CategoryRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new InvalidCategoryDataException("Category name cannot be empty");
        }
        if (request.getType() == null) {
            throw new InvalidCategoryDataException("Category type cannot be null");
        }
        if (categoryRepository.existsByNameAndType(request.getName(), request.getType())) {
            throw new InvalidCategoryDataException("Category with this name and type already exists");
        }
    }
}