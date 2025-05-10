package com.turing.expensetracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.turing.expensetracker.entity.Category;
import com.turing.expensetracker.enums.CategoryType;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByType(CategoryType type);
    
    boolean existsByNameAndType(String name, CategoryType type);


}
