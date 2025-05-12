package az.edu.msanalytics.controller;

import az.edu.msanalytics.model.dto.response.MonthlySummaryResponse;
import az.edu.msanalytics.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/monthly-summary")
    public List<MonthlySummaryResponse> getMonthlySummary(@RequestParam Long userId,
                                                          @RequestParam int year) {
        return analyticsService.getMonthlySummary(userId, year);
    }
}
