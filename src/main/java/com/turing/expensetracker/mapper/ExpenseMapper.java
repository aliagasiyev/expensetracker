package com.turing.expensetracker.mapper;

import com.turing.expensetracker.dto.request.ExpenseRequest;
import com.turing.expensetracker.dto.response.ExpenseResponse;
import com.turing.expensetracker.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "isIncome", source = "income")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "date", source = "date")
    Expense toEntity(ExpenseRequest request);

    @Mapping(target = "isIncome", source = "income")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "description", ignore = true)
    ExpenseResponse toResponse(Expense entity);
}