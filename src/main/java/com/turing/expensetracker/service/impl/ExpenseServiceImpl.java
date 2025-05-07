package com.turing.expensetracker.service.impl;
import com.turing.expensetracker.dto.request.ExpenseRequest;
import com.turing.expensetracker.dto.response.ExpenseResponse;
import com.turing.expensetracker.entity.Expense;
import com.turing.expensetracker.exception.ResourceNotFoundException;
import com.turing.expensetracker.mapper.ExpenseMapper;
import com.turing.expensetracker.repository.ExpenseRepository;
import com.turing.expensetracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request) {
        Expense expense = expenseMapper.toEntity(request);
        Expense saved = expenseRepository.save(expense);
        return expenseMapper.toResponse(saved);
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenseMapper.toResponseList(expenses);
    }

    @Override
    public List<ExpenseResponse> getExpensesByDateRange(LocalDate from, LocalDate to) {
        List<Expense> all = expenseRepository.findAll();
        List<Expense> filtered = all.stream()
            .filter(e -> !e.getDate().isBefore(from) && !e.getDate().isAfter(to))
            .collect(Collectors.toList());
        return expenseMapper.toResponseList(filtered);
    }

    @Override
    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        return expenseMapper.toResponse(expense);
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());
        expense.setIncome(request.isIncome());

        Expense updated = expenseRepository.save(expense);
        return expenseMapper.toResponse(updated);
    }

    @Override
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> getStatistics() {
        List<Expense> all = expenseRepository.findAll();

        BigDecimal totalIncome = all.stream()
                .filter(Expense::isIncome)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = all.stream()
                .filter(e -> !e.isIncome())
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Map.of(
                "totalIncome", totalIncome,
                "totalExpense", totalExpense
        );
    }
}
