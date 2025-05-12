package az.edu.msexpense.controller;

import az.edu.msexpense.dto.request.ExpenseRequest;
import az.edu.msexpense.dto.response.ExpenseResponse;
import az.edu.msexpense.enums.CategoryType;
import az.edu.msexpense.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(summary = "Create a new expense")
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request) {
        return new ResponseEntity<>(expenseService.createExpense(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all expenses")
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @Operation(summary = "Get expense by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @PathVariable(name = "id") 
            @Parameter(description = "ID of the expense to retrieve", required = true) 
            Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    @Operation(summary = "Get expenses within a date range")
    @GetMapping("/range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @Parameter(description = "Start date (ISO format)", required = true)
            @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date (ISO format)", required = true)
            @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(expenseService.getExpensesByDateRange(from, to));
    }

    @Operation(summary = "Update an expense")
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @Parameter(description = "ID of the expense to update", required = true)
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.updateExpense(id, request));
    }

    @Operation(summary = "Delete an expense by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @Parameter(description = "ID of the expense to delete", required = true)
            @PathVariable("id") Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get expense statistics")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(expenseService.getStatistics());
    }

    @Operation(summary = "Search expenses with filters")
    @GetMapping("/search")
    public ResponseEntity<List<ExpenseResponse>> searchExpenses(
            @Parameter(description = "Title to search for")
            @RequestParam(required = false) String title,
            
            @Parameter(description = "Category ID to filter by")
            @RequestParam(required = false) Long categoryId,
            
            @Parameter(description = "Date to filter by (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            
            @Parameter(description = "Minimum amount")
            @RequestParam(required = false) BigDecimal minAmount,
            
            @Parameter(description = "Maximum amount")
            @RequestParam(required = false) BigDecimal maxAmount) {
        return ResponseEntity.ok(expenseService.searchExpenses(title, categoryId, date, minAmount, maxAmount));
    }

    @Operation(summary = "Get today's expenses")
    @GetMapping("/today")
    public ResponseEntity<List<ExpenseResponse>> getTodayExpenses() {
        return ResponseEntity.ok(expenseService.getTodayExpenses());
    }

    @Operation(summary = "Get expenses grouped by category type")
    @GetMapping("/by-category")
    public ResponseEntity<Map<CategoryType, BigDecimal>> getExpensesByCategory() {
        return ResponseEntity.ok(expenseService.getExpensesByCategory());
    }

    @Operation(summary = "Get top 5 expenses by amount")
    @GetMapping("/top")
    public ResponseEntity<List<ExpenseResponse>> getTopExpenses() {
        return ResponseEntity.ok(expenseService.getTopExpenses());
    }
}