package com.turing.expensetracker.dto.response;

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

    private String category; 

    private LocalDate date; 

    private boolean isIncome; 
}
