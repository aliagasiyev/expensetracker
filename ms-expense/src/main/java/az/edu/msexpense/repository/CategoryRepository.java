package az.edu.msexpense.repository;

import java.util.List;

import az.edu.msexpense.entity.Category;
import az.edu.msexpense.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByType(CategoryType type);

    boolean existsByNameAndType(String name, CategoryType type);


}
