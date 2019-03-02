package org.venkat.freshfruits.mappers;

import org.junit.Test;
import org.venkat.freshfruits.dto.CategoryDTO;
import org.venkat.freshfruits.entity.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategoryMapperTest {
    private static final String CATEGORY_NAME = "Fruits";
    private static final Long CATEGORY_ID = 1L;

    @Test
    public void dtoToEntity() {
        //Given
        final CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(CATEGORY_NAME);
        categoryDTO.setId(CATEGORY_ID);

        //when
        final Category category = CategoryMapper.CATEGORY_MAPPER.dtoToEntity(categoryDTO);

        //then
        assertNotNull(category);
        assertEquals(CATEGORY_ID, category.getId());
        assertEquals(CATEGORY_NAME, category.getName());
    }

    @Test
    public void entityToDto() {
        //Given
        final Category category = new Category();
        category.setName(CATEGORY_NAME);
        category.setId(CATEGORY_ID);

        //when
        final CategoryDTO categoryDTO = CategoryMapper.CATEGORY_MAPPER.entityToDto(category);

        //then
        assertNotNull(categoryDTO);
        assertEquals(CATEGORY_ID, categoryDTO.getId());
        assertEquals(CATEGORY_NAME, categoryDTO.getName());
    }

}