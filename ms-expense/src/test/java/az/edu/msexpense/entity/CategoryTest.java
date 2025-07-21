package az.edu.msexpense.entity;


import az.edu.msexpense.enums.CategoryType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    @Test
    void testCategoryCreationWithValidData() {
        Category category = Category.builder()
                .id(1L)
                .name("Food")
                .type(CategoryType.EXPENSE)
                .color("#FF0000")
                .build();
        assertNotNull(category);
        assertEquals(1L, category.getId());
        assertEquals("Food", category.getName());
        assertEquals(CategoryType.EXPENSE, category.getType());
        assertEquals("#FF0000", category.getColor());
    }

    @Test
    void testCategoryCreationWithNullFields() {
        Category category = Category.builder().build();
        assertNull(category.getId());
        assertNull(category.getName());
        assertNull(category.getType());
        assertNull(category.getColor());
    }

    @Test
    void testCategoryUpdateFields() {
        Category category = Category.builder().name("Old").build();
        category.setName("New");
        assertEquals("New", category.getName());
    }

    @Test
    void testCategoryEqualsAndHashCode() {
        Category c1 = Category.builder().id(1L).name("A").type(CategoryType.EXPENSE).build();
        Category c2 = Category.builder().id(1L).name("A").type(CategoryType.EXPENSE).build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}
