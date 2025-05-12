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
}
