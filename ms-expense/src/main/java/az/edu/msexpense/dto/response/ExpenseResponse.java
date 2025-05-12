package az.edu.msexpense.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponse {

    private Long id; 

    private String title;

    private BigDecimal amount; 

    private CategoryResponse category;

    private LocalDate date; 

    private boolean isIncome; 

    private String description;

}
