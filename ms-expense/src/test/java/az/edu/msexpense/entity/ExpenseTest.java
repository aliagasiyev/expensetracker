package az.edu.msexpense.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    @Test
    void testExpenseCreationWithValidData() {
        Category category = Category.builder().id(1L).name("Food").type(CategoryType.EXPENSE).color("#FF0000").build();
        Expense expense = Expense.builder()
                .id(1L)
                .title("Lunch")
                .amount(BigDecimal.valueOf(15.5))
                .userId(100L)
                .category(category)
                .date(LocalDate.now())
                .income(false)
                .description("Lunch at cafe")
                .build();
        assertNotNull(expense);
        assertEquals("Lunch", expense.getTitle());
        assertEquals(BigDecimal.valueOf(15.5), expense.getAmount());
        assertEquals(100L, expense.getUserId());
        assertEquals(category, expense.getCategory());
        assertFalse(expense.isIncome());
        assertEquals("Lunch at cafe", expense.getDescription());
    }

    @Test
    void testExpenseCreationWithNullFields() {
        Expense expense = Expense.builder().build();
        assertNull(expense.getTitle());
        assertNull(expense.getAmount());
        assertNull(expense.getUserId());
        assertNull(expense.getCategory());
        assertNull(expense.getDate());
        assertFalse(expense.isIncome());
        assertNull(expense.getDescription());
    }

    @Test
    void testExpenseNegativeAmount() {
        Expense expense = Expense.builder().amount(BigDecimal.valueOf(-10)).build();
        assertTrue(expense.getAmount().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void testExpenseUpdateFields() {
        Expense expense = Expense.builder().title("Old").amount(BigDecimal.ONE).build();
        expense.setTitle("New");
        expense.setAmount(BigDecimal.TEN);
        assertEquals("New", expense.getTitle());
        assertEquals(BigDecimal.TEN, expense.getAmount());
    }

    @Test
    void testExpenseEqualsAndHashCode() {
        Expense e1 = Expense.builder().id(1L).title("A").build();
        Expense e2 = Expense.builder().id(1L).title("A").build();
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }
}
