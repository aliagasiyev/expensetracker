package az.edu.msexpense.controller;



import az.edu.msexpense.dto.request.CategoryRequest;
import az.edu.msexpense.dto.response.CategoryResponse;
import az.edu.msexpense.enums.CategoryType;
import az.edu.msexpense.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        return new ResponseEntity<>(categoryService.createCategory(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    @Operation(summary = "Get categories by type")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByType(
            @PathVariable(name = "type") 
            @Parameter(description = "Type of categories to retrieve (INCOME or EXPENSE)", required = true)
            CategoryType type) {
        return ResponseEntity.ok(categoryService.getCategoriesByType(type));
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable(name = "id") 
            @Parameter(description = "ID of the category to retrieve", required = true)
            Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Update a category")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @Parameter(description = "ID of the category to update", required = true)
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @Operation(summary = "Delete a category")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true)
            @PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}