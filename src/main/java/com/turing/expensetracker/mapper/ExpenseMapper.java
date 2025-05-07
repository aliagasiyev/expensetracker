package com.turing.expensetracker.mapper;

import com.turing.expensetracker.dto.request.ExpenseRequest;
import com.turing.expensetracker.dto.response.ExpenseResponse;
import com.turing.expensetracker.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isIncome", source = "income")
    Expense toEntity(ExpenseRequest request);

    @Mapping(target = "isIncome", source = "income")
    ExpenseResponse toResponse(Expense entity);

    List<ExpenseResponse> toResponseList(List<Expense> entities);
}
