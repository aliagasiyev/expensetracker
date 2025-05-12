package az.edu.msexpense.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Date is required")
    private LocalDate date; 

    private boolean isIncome;

    @NotNull(message = "Description is required")
    private String description;


}
