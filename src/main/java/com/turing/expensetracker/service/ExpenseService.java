package com.turing.expensetracker.service;

import com.turing.expensetracker.dto.request.ExpenseRequest;
import com.turing.expensetracker.dto.response.ExpenseResponse;
import com.turing.expensetracker.enums.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExpenseService {
    ExpenseResponse createExpense(ExpenseRequest request);

    List<ExpenseResponse> getAllExpenses();

    List<ExpenseResponse> getExpensesByDateRange(LocalDate from, LocalDate to);

    ExpenseResponse getExpenseById(Long id);

    ExpenseResponse updateExpense(Long id, ExpenseRequest request);

    void deleteExpense(Long id);

    Map<String, Object> getStatistics();

    List<ExpenseResponse> searchExpenses(String title, Long categoryId, LocalDate date,
                                       BigDecimal minAmount, BigDecimal maxAmount);

    List<ExpenseResponse> getTodayExpenses();

    Map<CategoryType, BigDecimal> getExpensesByCategory();

    List<ExpenseResponse> getTopExpenses();
}