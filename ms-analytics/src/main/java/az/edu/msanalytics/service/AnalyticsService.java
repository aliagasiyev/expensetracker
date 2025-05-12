package az.edu.msanalytics.service;

import az.edu.msanalytics.model.dto.response.MonthlySummaryResponse;

import java.util.List;

public interface AnalyticsService {
    List<MonthlySummaryResponse> getMonthlySummary(Long userId, int year);

}
