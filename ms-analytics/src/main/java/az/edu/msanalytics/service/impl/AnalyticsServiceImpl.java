package az.edu.msanalytics.service.impl;

import az.edu.msanalytics.client.ExpenseClient;
import az.edu.msanalytics.model.dto.request.ExpenseDto;
import az.edu.msanalytics.model.dto.response.MonthlySummaryResponse;
import az.edu.msanalytics.service.AnalyticsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ExpenseClient expenseClient;

    public AnalyticsServiceImpl(ExpenseClient expenseClient) {
        this.expenseClient = expenseClient;
    }

    @Override
    public List<MonthlySummaryResponse> getMonthlySummary(Long userId, int year) {
        List<ExpenseDto> expenses = expenseClient.getExpensesByUser(userId, year);

        Map<Month, List<ExpenseDto>> grouped = expenses.stream()
                .collect(Collectors.groupingBy(e -> e.getDate().getMonth()));

        List<MonthlySummaryResponse> result = new ArrayList<>();

        for (Month month : Month.values()) {
            List<ExpenseDto> monthly = grouped.getOrDefault(month, Collections.emptyList());

            BigDecimal totalIncome = monthly.stream()
                    .filter(ExpenseDto::isIncome)
                    .map(ExpenseDto::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalExpense = monthly.stream()
                    .filter(e -> !e.isIncome())
                    .map(ExpenseDto::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            result.add(new MonthlySummaryResponse(month.name(), totalIncome, totalExpense));
        }

        return result;
    }

    @Override
    public Map<String, Object> getUserSummary(Long userId) {
        List<ExpenseDto> expenses = expenseClient.getExpensesByUser(userId, 2024);
        
        BigDecimal totalIncome = expenses.stream()
                .filter(ExpenseDto::isIncome)
                .map(ExpenseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = expenses.stream()
                .filter(e -> !e.isIncome())
                .map(ExpenseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> summary = new HashMap<>();
        summary.put("userId", userId);
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("balance", totalIncome.subtract(totalExpense));
        summary.put("transactionCount", expenses.size());
        
        return summary;
    }

    @Override
    public Map<String, Object> getExpensesByCategory(Long userId) {
        List<ExpenseDto> expenses = expenseClient.getExpensesByUser(userId, 2024);
        
        Map<String, BigDecimal> categoryTotals = expenses.stream()
                .filter(e -> !e.isIncome())
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, ExpenseDto::getAmount, BigDecimal::add)
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("categories", categoryTotals);
        
        return result;
    }

    @Override
    public Map<String, Object> getChartData(Long userId) {
        List<ExpenseDto> expenses = expenseClient.getExpensesByUser(userId, 2024);
        
        Map<String, BigDecimal> monthlyData = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate().getMonth().name(),
                        Collectors.reducing(BigDecimal.ZERO, ExpenseDto::getAmount, BigDecimal::add)
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("monthlyData", monthlyData);
        result.put("labels", monthlyData.keySet());
        result.put("values", monthlyData.values());
        
        return result;
    }

    @Override
    public Map<String, Object> compareMonths(Long userId) {
        List<ExpenseDto> expenses = expenseClient.getExpensesByUser(userId, 2024);
        
        Map<Month, BigDecimal> monthlyTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate().getMonth(),
                        Collectors.reducing(BigDecimal.ZERO, ExpenseDto::getAmount, BigDecimal::add)
                ));

        Month currentMonth = Month.values()[new Date().getMonth()];
        Month previousMonth = currentMonth.minus(1);
        
        BigDecimal currentTotal = monthlyTotals.getOrDefault(currentMonth, BigDecimal.ZERO);
        BigDecimal previousTotal = monthlyTotals.getOrDefault(previousMonth, BigDecimal.ZERO);
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("currentMonth", currentMonth.name());
        result.put("currentTotal", currentTotal);
        result.put("previousMonth", previousMonth.name());
        result.put("previousTotal", previousTotal);
        result.put("difference", currentTotal.subtract(previousTotal));
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getTopExpenses(Long userId, int limit) {
        List<ExpenseDto> expenses = expenseClient.getExpensesByUser(userId, 2024);
        
        return expenses.stream()
                .filter(e -> !e.isIncome())
                .sorted((e1, e2) -> e2.getAmount().compareTo(e1.getAmount()))
                .limit(limit)
                .map(expense -> {
                    Map<String, Object> expenseMap = new HashMap<>();
                    expenseMap.put("id", expense.getId());
                    expenseMap.put("title", expense.getTitle());
                    expenseMap.put("amount", expense.getAmount());
                    expenseMap.put("category", expense.getCategory());
                    expenseMap.put("date", expense.getDate());
                    return expenseMap;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getSystemOverview() {
        // For admin - system-wide statistics
        // This would typically aggregate data from all users
        Map<String, Object> overview = new HashMap<>();
        overview.put("totalUsers", 100); // Mock data
        overview.put("totalTransactions", 5000); // Mock data
        overview.put("totalAmount", new BigDecimal("250000")); // Mock data
        overview.put("activeUsers", 85); // Mock data
        
        return overview;
    }

    @Override
    public List<Map<String, Object>> getAllUsersSummary() {
        // For admin - summary of all users
        // This would typically fetch data for all users
        List<Map<String, Object>> usersSummary = new ArrayList<>();
        
        // Mock data for demonstration
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> userSummary = new HashMap<>();
            userSummary.put("userId", (long) i);
            userSummary.put("email", "user" + i + "@example.com");
            userSummary.put("totalExpenses", new BigDecimal(1000 * i));
            userSummary.put("transactionCount", 50 * i);
            usersSummary.add(userSummary);
        }
        
        return usersSummary;
    }
}
