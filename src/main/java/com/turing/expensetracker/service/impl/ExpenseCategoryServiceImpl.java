package com.turing.expensetracker.service.impl;

import com.turing.expensetracker.dto.request.ExpenseCategoryRequest;
import com.turing.expensetracker.dto.response.ExpenseCategoryResponse;
import com.turing.expensetracker.entity.ExpenseCategory;
import com.turing.expensetracker.exception.CategoryNotFoundException;
import com.turing.expensetracker.exception.InvalidCategoryDataException;
import com.turing.expensetracker.mapper.ExpenseCategoryMapper;
import com.turing.expensetracker.repository.ExpenseCategoryRepository;
import com.turing.expensetracker.service.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseCategoryMapper categoryMapper;

    @Override
    public ExpenseCategoryResponse createCategory(Long userId, ExpenseCategoryRequest request) {
        validateCategoryData(request);
        
        // Check if category with same name already exists for this user
        if (categoryRepository.existsByNameAndUserId(request.getName(), userId)) {
            throw new InvalidCategoryDataException("Category with name '" + request.getName() + "' already exists");
        }

        ExpenseCategory category = categoryMapper.toEntity(request);
        category.setUserId(userId);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseCategoryResponse> getAllCategories(Long userId) {
        return categoryRepository.findByUserId(userId).stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseCategoryResponse getCategoryById(Long userId, Long id) {
        ExpenseCategory category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return categoryMapper.toResponse(category);
    }

    @Override
    public ExpenseCategoryResponse updateCategory(Long userId, Long id, ExpenseCategoryRequest request) {
        validateCategoryData(request);
        
        ExpenseCategory existingCategory = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        // Check if new name conflicts with another category
        if (!existingCategory.getName().equals(request.getName()) && 
            categoryRepository.existsByNameAndUserId(request.getName(), userId)) {
            throw new InvalidCategoryDataException("Category with name '" + request.getName() + "' already exists");
        }

        ExpenseCategory updatedCategory = categoryMapper.toEntity(request);
        updatedCategory.setId(existingCategory.getId());
        updatedCategory.setUserId(userId);
        return categoryMapper.toResponse(categoryRepository.save(updatedCategory));
    }

    @Override
    public void deleteCategory(Long userId, Long id) {
        if (!categoryRepository.existsByIdAndUserId(id, userId)) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }

    private void validateCategoryData(ExpenseCategoryRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new InvalidCategoryDataException("Category name cannot be empty");
        }
        if (request.getName().length() > 50) {
            throw new InvalidCategoryDataException("Category name cannot be longer than 50 characters");
        }
        if (request.getDescription() != null && request.getDescription().length() > 200) {
            throw new InvalidCategoryDataException("Category description cannot be longer than 200 characters");
        }
    }
} 