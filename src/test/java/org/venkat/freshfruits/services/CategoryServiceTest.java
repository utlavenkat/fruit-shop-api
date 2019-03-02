package org.venkat.freshfruits.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.venkat.freshfruits.dto.CategoriesDataDTO;
import org.venkat.freshfruits.dto.CategoryDTO;
import org.venkat.freshfruits.entity.Category;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.mappers.CategoryMapper;
import org.venkat.freshfruits.repositories.CategoryRepository;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CategoryServiceTest {

    private static final String CATEGORY_FRUITS = "Fruits";
    private static final Long FRUITS_ID = 1L;
    private static final String CATEGORIES_BASE_URL = "/shop/categories/";

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    private CategoryService categoryService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryService = new CategoryService(categoryRepository, categoryMapper);
    }

    @Test
    public void getAllCategories() {

        //Given
        Category fruits = new Category();
        fruits.setId(FRUITS_ID);
        fruits.setName(CATEGORY_FRUITS);

        CategoryDTO fruitsDTO = new CategoryDTO();
        fruitsDTO.setName(CATEGORY_FRUITS);
        fruitsDTO.setId(FRUITS_ID);


        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(fruits));
        when(categoryMapper.entityToDto(any(Category.class))).thenReturn(fruitsDTO);

        //when
        CategoriesDataDTO categoryDataDto = categoryService.getAllCategories();

        //then
        assertNotNull("Null Category List", categoryDataDto.getCategories());
        assertEquals("Category List size does not match", 1, categoryDataDto.getCategories().size());
        CategoryDTO savedCategory = categoryDataDto.getCategories().get(0);
        assertEquals("Category name did not match", CATEGORY_FRUITS, savedCategory.getName());
        assertEquals("category url did not match", CATEGORIES_BASE_URL + CATEGORY_FRUITS, savedCategory.getCategoryUrl());
        assertEquals("Category ID did not match", FRUITS_ID.longValue(), savedCategory.getId().longValue());
    }

    @Test
    public void findCategoryByName() {
        //Given
        Category fruits = new Category();
        fruits.setId(FRUITS_ID);
        fruits.setName(CATEGORY_FRUITS);

        CategoryDTO fruitsDTO = new CategoryDTO();
        fruitsDTO.setName(CATEGORY_FRUITS);
        fruitsDTO.setId(FRUITS_ID);

        when(categoryRepository.findByNameIgnoreCase(anyString())).thenReturn(fruits);
        when(categoryMapper.entityToDto(any(Category.class))).thenReturn(fruitsDTO);


        //when
        CategoryDTO categoryDTO = categoryService.findCategoryByName(CATEGORY_FRUITS);

        //then
        assertNotNull(categoryDTO);
        assertEquals("Category name did not match", CATEGORY_FRUITS, categoryDTO.getName());
        assertEquals("Category ID did not match", FRUITS_ID.longValue(), categoryDTO.getId().longValue());
    }

    @Test(expected = NotFoundException.class)
    public void findByUnknownCategory() {

        //Given
        when(categoryRepository.findByNameIgnoreCase(anyString())).thenReturn(null);

        //when
        CategoryDTO categoryDTO = categoryService.findCategoryByName(CATEGORY_FRUITS);

    }
}