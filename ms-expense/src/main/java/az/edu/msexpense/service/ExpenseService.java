package az.edu.msexpense.service;



import az.edu.msexpense.dto.request.ExpenseRequest;
import az.edu.msexpense.dto.response.ExpenseResponse;
import az.edu.msexpense.enums.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExpenseService {
    ExpenseResponse createExpense(ExpenseRequest request, Long userId);

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

    List<ExpenseResponse> getUserExpenses(Long userId);
    List<ExpenseResponse> getTopExpenses();
}