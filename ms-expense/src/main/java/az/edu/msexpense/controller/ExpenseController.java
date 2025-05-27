package az.edu.msexpense.controller;

import az.edu.msexpense.client.TokenValidationResponse;
import az.edu.msexpense.dto.request.ExpenseRequest;
import az.edu.msexpense.dto.response.ExpenseResponse;
import az.edu.msexpense.enums.CategoryType;
import az.edu.msexpense.service.ExpenseExportService;
import az.edu.msexpense.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Expenses", description = "Expense management APIs")
public class ExpenseController {

    private final ExpenseService expenseService;
   private final ExpenseExportService expenseExportService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = (TokenValidationResponse) auth.getPrincipal();
        Long userId = user.getUserId();

        return new ResponseEntity<>(expenseService.createExpense(request, userId), HttpStatus.CREATED);
    }

    @Operation(summary = "Export user's expenses as PDF and save to local file")
    @GetMapping("/export/pdf/save")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> exportAndSaveExpensesPdf() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = (TokenValidationResponse) auth.getPrincipal();
        Long userId = user.getUserId();

        List<ExpenseResponse> expenses = expenseService.getUserExpenses(userId);
        String fileName = "expenses_" + userId + "_" + LocalDate.now() + ".pdf";
        String filePath = expenseExportService.saveExpensesPdfToFile(expenses, fileName);

        return ResponseEntity.ok("PDF saved to: " + filePath);
    }

    @Operation(summary = "Get user's expenses")
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ExpenseResponse>> getUserExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @Operation(summary = "Get all expenses (Admin only)")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @Operation(summary = "Get expense by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @Parameter(description = "ID of the expense to update", required = true)
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.updateExpense(id, request));
    }

    @Operation(summary = "Delete an expense by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = (TokenValidationResponse) auth.getPrincipal();
        return ResponseEntity.ok("Hello from expense service, " + user.getEmail());
    }

    @Operation(summary = "Export user's expenses as PDF")
    @GetMapping("/export/pdf")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportExpensesToPdf() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = (TokenValidationResponse) auth.getPrincipal();
        Long userId = user.getUserId();

        List<ExpenseResponse> expenses = expenseService.getUserExpenses(userId);
        byte[] pdfBytes = expenseExportService.exportExpensesToPdf(expenses);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("expenses.pdf").build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

    }
}