package com.turing.expensetracker.service.impl;

import com.turing.expensetracker.dto.request.ExpenseRequest;
import com.turing.expensetracker.dto.response.ExpenseResponse;
import com.turing.expensetracker.entity.Category;
import com.turing.expensetracker.entity.Expense;
import com.turing.expensetracker.enums.CategoryType;
import com.turing.expensetracker.exception.*;
import com.turing.expensetracker.mapper.ExpenseMapper;
import com.turing.expensetracker.repository.CategoryRepository;
import com.turing.expensetracker.repository.ExpenseRepository;
import com.turing.expensetracker.service.ExpenseService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request) {
        try {
            validateExpenseData(request);
    
            Expense expense = expenseMapper.toEntity(request);
    
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new InvalidCategoryDataException("Category not found with ID: " + request.getCategoryId()));
            expense.setCategory(category);
    
            return expenseMapper.toResponse(expenseRepository.save(expense));
        } catch (ExpenseNotFoundException | InvalidCategoryDataException | InvalidDateRangeException |
                 InvalidExpenseDataException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidExpenseDataException("Failed to create expense: " + e.getMessage());
        }
    }
    

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses() {
        try {
            List<Expense> expenses = expenseRepository.findAll();
            if (expenses.isEmpty()) {
                throw new ResourceNotFoundException("No expenses found");
            }
            return expenses.stream()
                    .map(expenseMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidExpenseDataException("Failed to retrieve expenses: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {
        try {
            return expenseRepository.findById(id)
                    .map(expenseMapper::toResponse)
                    .orElseThrow(() -> new ExpenseNotFoundException(id));
        } catch (ExpenseNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidExpenseDataException("Failed to retrieve expense with id " + id + ": " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByDateRange(LocalDate from, LocalDate to) {
        try {
            validateDateRange(from, to);
            List<Expense> expenses = expenseRepository.findByDateBetween(from, to);
            if (expenses.isEmpty()) {
                throw new ResourceNotFoundException("No expenses found in the specified date range");
            }
            return expenses.stream()
                    .map(expenseMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException | InvalidDateRangeException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidExpenseDataException("Failed to retrieve expenses by date range: " + e.getMessage());
        }
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        try {
            validateExpenseData(request);
            Expense existingExpense = expenseRepository.findById(id)
                    .orElseThrow(() -> new ExpenseNotFoundException(id));
            
            Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));
            
            Expense updatedExpense = expenseMapper.toEntity(request);
            updatedExpense.setId(existingExpense.getId());
            updatedExpense.setCategory(category);
            
            return expenseMapper.toResponse(expenseRepository.save(updatedExpense));
        } catch (ExpenseNotFoundException | CategoryNotFoundException | 
                InvalidExpenseDataException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidExpenseDataException("Failed to update expense: " + e.getMessage());
        }
    }

    @Override
    public void deleteExpense(Long id) {
        try {
            if (!expenseRepository.existsById(id)) {
                throw new ExpenseNotFoundException(id);
            }
            expenseRepository.deleteById(id);
        } catch (ExpenseNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidExpenseDataException("Failed to delete expense: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics() {
        try {
            List<Expense> expenses = expenseRepository.findAll();
            if (expenses.isEmpty()) {
                throw new ResourceNotFoundException("No expenses found to calculate statistics");
            }

            Map<String, Object> statistics = new HashMap<>();
            
            BigDecimal totalExpenses = expenses.stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            statistics.put("totalExpenses", totalExpenses);

            BigDecimal averageExpense = totalExpenses.divide(
                    BigDecimal.valueOf(expenses.size()), 2, RoundingMode.HALF_UP);
            statistics.put("averageExpense", averageExpense);

            BigDecimal highestExpense = expenses.stream()
                    .map(Expense::getAmount)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            statistics.put("highestExpense", highestExpense);

            BigDecimal lowestExpense = expenses.stream()
                    .map(Expense::getAmount)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            statistics.put("lowestExpense", lowestExpense);

            return statistics;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidExpenseDataException("Failed to calculate statistics: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> searchExpenses(String title, Long categoryId, LocalDate date,
                                              BigDecimal minAmount, BigDecimal maxAmount) {
        try {
            validateSearchCriteria(title, categoryId, date, minAmount, maxAmount);
            return expenseRepository.findAll().stream()
                    .filter(expense -> 
                        (title == null || expense.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (categoryId == null || expense.getCategory().getId().equals(categoryId)) &&
                        (date == null || expense.getDate().equals(date)) &&
                        (minAmount == null || expense.getAmount().compareTo(minAmount) >= 0) &&
                        (maxAmount == null || expense.getAmount().compareTo(maxAmount) <= 0))
                    .map(expenseMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (InvalidExpenseDataException | InvalidDateRangeException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidExpenseDataException("Failed to search expenses: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getTodayExpenses() {
        try {
            LocalDate today = LocalDate.now();
            List<Expense> expenses = expenseRepository.findByDateBetween(today, today);
            if (expenses.isEmpty()) {
                throw new ResourceNotFoundException("No expenses found for today");
            }
            return expenses.stream()
                    .map(expenseMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidExpenseDataException("Failed to retrieve today's expenses: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<CategoryType, BigDecimal> getExpensesByCategory() {
        List<Expense> expenses = expenseRepository.findAll();

        if (expenses.isEmpty()) {
            throw new ResourceNotFoundException("No expenses found to group by category");
        }

        return expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getType(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getTopExpenses() {
        try {
            List<Expense> expenses = expenseRepository.findAll();
            if (expenses.isEmpty()) {
                throw new ResourceNotFoundException("No expenses found to get top expenses");
            }
            return expenses.stream()
                    .sorted((e1, e2) -> e2.getAmount().compareTo(e1.getAmount()))
                    .limit(5)
                    .map(expenseMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidExpenseDataException("Failed to retrieve top expenses: " + e.getMessage());
        }
    }

    private void validateExpenseData(ExpenseRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidExpenseDataException("Amount must be greater than 0");
        }
        if (request.getDate() == null) {
            throw new InvalidExpenseDataException("Date cannot be null");
        }
        if (request.getDate().isAfter(LocalDate.now())) {
            throw new InvalidExpenseDataException("Date cannot be in the future");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new InvalidExpenseDataException("Description cannot be empty");
        }
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new InvalidExpenseDataException("Title cannot be empty");
        }
        if (request.getCategoryId() == null) {
            throw new InvalidCategoryDataException("Category ID cannot be null");
        }
    }
    

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new InvalidDateRangeException("Both 'from' and 'to' dates must be provided");
        }
        if (from.isAfter(to)) {
            throw new InvalidDateRangeException("Start date cannot be after end date");
        }
        if (from.isAfter(LocalDate.now()) || to.isAfter(LocalDate.now())) {
            throw new InvalidDateRangeException("Date range cannot include future dates");
        }
    }

    private void validateSearchCriteria(String title, Long categoryId, LocalDate date,
                                      BigDecimal minAmount, BigDecimal maxAmount) {
        if (title != null && title.trim().isEmpty()) {
            throw new InvalidExpenseDataException("Title cannot be empty");
        }
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
        if (date != null && date.isAfter(LocalDate.now())) {
            throw new InvalidDateRangeException("Search date cannot be in the future");
        }
        if (minAmount != null && minAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidExpenseDataException("Minimum amount cannot be negative");
        }
        if (maxAmount != null && maxAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidExpenseDataException("Maximum amount cannot be negative");
        }
        if (minAmount != null && maxAmount != null && minAmount.compareTo(maxAmount) > 0) {
            throw new InvalidExpenseDataException("Minimum amount cannot be greater than maximum amount");
        }
    }
}