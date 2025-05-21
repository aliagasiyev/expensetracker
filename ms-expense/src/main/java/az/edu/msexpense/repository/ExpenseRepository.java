package az.edu.msexpense.repository;

import az.edu.msexpense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByDateBetween(LocalDate from, LocalDate to);

    List<Expense> findByUserId(Long currentUserId);

    List<Expense> findByUserIdAndDateBetween(Long currentUserId, LocalDate from, LocalDate to);
} 