package org.venkat.freshfruits.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.venkat.freshfruits.dto.CategoriesDataDTO;
import org.venkat.freshfruits.dto.CategoryDTO;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.services.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest {

    private static final String CATEGORY_FRUITS = "Fruits";
    private static final Long FRUITS_ID = 1L;
    private static final String CATEGORIES_BASE_URL = "/shop/categories/";

    @Mock
    private CategoryService categoryService;


    private MockMvc mockMvc;

    private CategoryController categoryController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryController = new CategoryController(categoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).setControllerAdvice(new ExceptionControllerAdvise()).build();
    }

    @Test
    public void getAllCategories() throws Exception {
        //Given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(CATEGORY_FRUITS);
        categoryDTO.setId(FRUITS_ID);
        categoryDTO.setCategoryUrl(CATEGORIES_BASE_URL + CATEGORY_FRUITS);

        List<CategoryDTO> categoryDTOList = new ArrayList<>(1);
        categoryDTOList.add(categoryDTO);

        final CategoriesDataDTO categoriesDataDTO = new CategoriesDataDTO(categoryDTOList);

        when(categoryService.getAllCategories()).thenReturn(categoriesDataDTO);

        //when
        ResultActions resultActions = mockMvc.perform(get(CATEGORIES_BASE_URL));

        //then
        resultActions.andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();
        ObjectMapper mapper = new ObjectMapper();
        CategoriesDataDTO responseCategoriesData = mapper.readValue(mvcResult.getResponse().getContentAsString()
                , CategoriesDataDTO.class);
        assertNotNull("Response is null", responseCategoriesData);
        assertEquals("Incorrect results returns", 1, responseCategoriesData.getCategories().size());
    }

    @Test
    public void getCategory() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(CATEGORY_FRUITS);
        categoryDTO.setId(FRUITS_ID);

        when(categoryService.findCategoryByName(anyString())).thenReturn(categoryDTO);

        //when
        ResultActions resultActions = mockMvc.perform(get(CATEGORIES_BASE_URL + CATEGORY_FRUITS));

        //then
        resultActions.andExpect(status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("Response Received:::" + response);
        ObjectMapper mapper = new ObjectMapper();
        CategoryDTO responseDTO = mapper.readValue(response, CategoryDTO.class);
        assertNotNull("Response is Null", responseDTO);
        assertEquals("Category name not matching", CATEGORY_FRUITS, responseDTO.getName());
        assertEquals("ID is not matching", FRUITS_ID.longValue(), responseDTO.getId().longValue());
    }

    @Test
    public void getCategory_NotFound() throws Exception {
        //given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(CATEGORY_FRUITS);
        categoryDTO.setId(FRUITS_ID);

        when(categoryService.findCategoryByName(anyString())).thenThrow(new NotFoundException("No category exists with the name " + CATEGORY_FRUITS));

        //when
        ResultActions resultActions = mockMvc.perform(get(CATEGORIES_BASE_URL + CATEGORY_FRUITS));

        //then
        resultActions.andExpect(status().isNotFound());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map responseMap = mapper.readValue(response, Map.class);
        assertNotNull(responseMap);
    }
}