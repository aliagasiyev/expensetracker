package com.turing.expensetracker.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.turing.expensetracker.dto.request.ExpenseCategoryRequest;
import com.turing.expensetracker.dto.response.ExpenseCategoryResponse;
import com.turing.expensetracker.entity.ExpenseCategory;


@Mapper(componentModel = "spring")
public interface ExpenseCategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    ExpenseCategory toEntity(ExpenseCategoryRequest request);

    ExpenseCategoryResponse toResponse(ExpenseCategory category);

    List<ExpenseCategoryResponse> toDtoList(List<ExpenseCategory> categories);
}