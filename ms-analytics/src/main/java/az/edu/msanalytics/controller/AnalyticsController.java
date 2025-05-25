package az.edu.msanalytics.controller;

import az.edu.msanalytics.model.dto.response.MonthlySummaryResponse;
import az.edu.msanalytics.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Analytics", description = "Analytics and reporting APIs")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // ✅ USER yalnız öz statistikasını görə bilər
    @Operation(summary = "Get monthly summary for user")
    @GetMapping("/monthly-summary")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<List<MonthlySummaryResponse>> getMonthlySummary(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Year") @RequestParam int year) {
        return ResponseEntity.ok(analyticsService.getMonthlySummary(userId, year));
    }

    // ✅ USER yalnız öz xülasəsini görə bilər
    @Operation(summary = "Get user summary")
    @GetMapping("/summary/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<Map<String, Object>> getUserSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(analyticsService.getUserSummary(userId));
    }

    // ✅ USER yalnız öz kateqoriya analitikasını görə bilər
    @Operation(summary = "Get expenses by category for user")
    @GetMapping("/by-category/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<Map<String, Object>> getExpensesByCategory(@PathVariable Long userId) {
        return ResponseEntity.ok(analyticsService.getExpensesByCategory(userId));
    }

    // ✅ USER yalnız öz qrafik məlumatlarını görə bilər
    @Operation(summary = "Get chart data for user")
    @GetMapping("/chart-data/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<Map<String, Object>> getChartData(@PathVariable Long userId) {
        return ResponseEntity.ok(analyticsService.getChartData(userId));
    }

    // ✅ USER yalnız öz müqayisəsini görə bilər
    @Operation(summary = "Compare months for user")
    @GetMapping("/compare-months/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<Map<String, Object>> compareMonths(@PathVariable Long userId) {
        return ResponseEntity.ok(analyticsService.compareMonths(userId));
    }

    // ✅ USER yalnız öz top xərclərini görə bilər
    @Operation(summary = "Get top expenses for user")
    @GetMapping("/top-expenses/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public ResponseEntity<List<Map<String, Object>>> getTopExpenses(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(analyticsService.getTopExpenses(userId, limit));
    }

    // ✅ Yalnız admin sistem statistikasını görə bilər
    @Operation(summary = "Get system overview (Admin only)")
    @GetMapping("/system-overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSystemOverview() {
        return ResponseEntity.ok(analyticsService.getSystemOverview());
    }

    // ✅ Yalnız admin bütün istifadəçi statistikasını görə bilər
    @Operation(summary = "Get all users summary (Admin only)")
    @GetMapping("/all-users-summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllUsersSummary() {
        return ResponseEntity.ok(analyticsService.getAllUsersSummary());
    }
}
