package az.edu.msexpense.mapper;

import az.edu.msexpense.dto.request.ExpenseRequest;
import az.edu.msexpense.dto.response.ExpenseResponse;
import az.edu.msexpense.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "income", source = "income")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "date", source = "date")
    Expense toEntity(ExpenseRequest request);

    @Mapping(target = "income", source = "income")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "description", ignore = true)
    ExpenseResponse toResponse(Expense entity);
}