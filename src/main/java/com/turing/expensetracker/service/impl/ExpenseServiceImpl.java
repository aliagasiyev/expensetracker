package com.turing.expensetracker.service.impl;

import com.turing.expensetracker.dto.request.ExpenseRequest;
import com.turing.expensetracker.dto.response.ExpenseResponse;
import com.turing.expensetracker.entity.Expense;
import com.turing.expensetracker.exception.ExpenseNotFoundException;
import com.turing.expensetracker.exception.InvalidDateRangeException;
import com.turing.expensetracker.exception.InvalidExpenseDataException;
import com.turing.expensetracker.mapper.ExpenseMapper;
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
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request) {
        validateExpenseData(request);
        Expense expense = expenseMapper.toEntity(request);
        return expenseMapper.toResponse(expenseRepository.save(expense));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .map(expenseMapper::toResponse)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByDateRange(LocalDate from, LocalDate to) {
        validateDateRange(from, to);
        return expenseRepository.findByDateBetween(from, to).stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        validateExpenseData(request);
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
        
        Expense updatedExpense = expenseMapper.toEntity(request);
        updatedExpense.setId(existingExpense.getId());
        return expenseMapper.toResponse(expenseRepository.save(updatedExpense));
    }

    @Override
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ExpenseNotFoundException(id);
        }
        expenseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics() {
        List<Expense> expenses = expenseRepository.findAll();
        if (expenses.isEmpty()) {
            throw new InvalidExpenseDataException("No expenses found to calculate statistics");
        }

        Map<String, Object> statistics = new HashMap<>();
        
        // Calculate total expenses
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
        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new InvalidExpenseDataException("Category cannot be empty");
        }
    }

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new InvalidDateRangeException("Both 'from' and 'to' dates must be provided");
        }
        if (from.isAfter(to)) {
            throw new InvalidDateRangeException();
        }
        if (from.isAfter(LocalDate.now()) || to.isAfter(LocalDate.now())) {
            throw new InvalidDateRangeException("Date range cannot include future dates");
        }
    }
} 