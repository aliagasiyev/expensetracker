package az.edu.msanalytics.service;

import az.edu.msanalytics.model.dto.response.MonthlySummaryResponse;

import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    List<MonthlySummaryResponse> getMonthlySummary(Long userId, int year);
    
    Map<String, Object> getUserSummary(Long userId);
    
    Map<String, Object> getExpensesByCategory(Long userId);
    
    Map<String, Object> getChartData(Long userId);
    
    Map<String, Object> compareMonths(Long userId);
    
    List<Map<String, Object>> getTopExpenses(Long userId, int limit);
    
    Map<String, Object> getSystemOverview();
    
    List<Map<String, Object>> getAllUsersSummary();
}
