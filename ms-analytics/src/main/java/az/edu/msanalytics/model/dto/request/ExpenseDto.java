package az.edu.msanalytics.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseDto {
    private Long id;
    private String title;
    private BigDecimal amount;
    private LocalDate date;

    @JsonProperty("isIncome")
    private boolean income;

    private CategoryDto category;
}
