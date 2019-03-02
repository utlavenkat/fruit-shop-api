package org.venkat.freshfruits.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.venkat.freshfruits.entity.Category;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryRepositoryIT {
    private static final String CATEGORY_NAME = "Berries";

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void save() {
        //Given
        Category category = new Category();
        category.setName(CATEGORY_NAME);

        //when
        Category savedCategory = categoryRepository.save(category);

        //then
        assertNotNull(savedCategory);
        assertTrue(savedCategory.getId() > 0);
        assertEquals(CATEGORY_NAME, savedCategory.getName());
    }

    @Test
    public void getAllCategories() {
        //when
        Iterable<Category> categories = categoryRepository.findAll();

        //then
        assertNotNull("No Categories", categories);
        List<Category> categoryList = StreamSupport.stream(categories.spliterator(), false)
                .collect(Collectors.toList());
        assertTrue("Empty Categories", categoryList.size() > 0);

    }

    @Test
    public void getCatgeoryByName() {
        //when
        Category category = categoryRepository.findByNameIgnoreCase(CATEGORY_NAME);

        //then
        assertNotNull(category);
        assertEquals("Category Name mismatch", CATEGORY_NAME, category.getName());
        assertTrue("Category ID is null", category.getId() > 0);
    }
}