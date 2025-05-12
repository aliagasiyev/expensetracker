package az.edu.msanalytics.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlySummaryResponse {
    private String month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
}
