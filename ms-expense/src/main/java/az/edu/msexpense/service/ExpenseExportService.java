package az.edu.msexpense.service;

import java.util.List;

import az.edu.msexpense.dto.response.ExpenseResponse;

public interface ExpenseExportService {
    byte[] exportExpensesToPdf(List<ExpenseResponse> expenses);

    String saveExpensesPdfToFile(List<ExpenseResponse> expenses, String fileName);
}