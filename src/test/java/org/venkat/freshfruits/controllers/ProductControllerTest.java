package org.venkat.freshfruits.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.venkat.freshfruits.dto.ProductDTO;
import org.venkat.freshfruits.dto.ProductsListDataDTO;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.services.ProductService;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    private static final String PRODUCTS_BASE_URL = "/shop/products";
    private static final String PRODUCT_NAME = "Test Product";
    private static final Long PRODUCT_ID = 100L;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;
    private ProductController productController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productController = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ExceptionControllerAdvise())
                .build();
    }

    @Test
    public void getAllProducts() throws Exception {
        //Given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(PRODUCT_NAME);
        productDTO.setProductUrl(PRODUCTS_BASE_URL + "/" + PRODUCT_ID);

        when(productService.findAllProducts()).thenReturn(new ProductsListDataDTO(Collections.singletonList(productDTO)));

        //when
        final ResultActions resultActions = mockMvc.perform(get(PRODUCTS_BASE_URL));

        //then
        resultActions.andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        ProductsListDataDTO productsListDataDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ProductsListDataDTO.class);

        assertNotNull("Response is null", productsListDataDTO);
        assertEquals("Incorrect results returned", 1, productsListDataDTO.getProducts().size());
    }

    @Test
    public void getProductById() throws Exception {
        //Given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(PRODUCT_NAME);
        when(productService.findById(anyLong())).thenReturn(productDTO);

        //when
        ResultActions resultActions = mockMvc.perform(get(PRODUCTS_BASE_URL + "/1"));

        //then
        resultActions.andExpect(status().isOk());
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO responseProductDTO = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString()
                , ProductDTO.class);
        assertNotNull("Product is null", responseProductDTO);
    }

    @Test
    public void getProductById_InvalidId() throws Exception {
        //Given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(PRODUCT_NAME);
        when(productService.findById(anyLong())).thenThrow(new NotFoundException("No product found for the given id"));

        //when
        ResultActions resultActions = mockMvc.perform(get(PRODUCTS_BASE_URL + "/1"));

        //then
        resultActions.andExpect(status().isNotFound());

    }

    @Test
    public void createProduct() throws Exception {
        ProductDTO product1 = new ProductDTO();
        product1.setName(PRODUCT_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        when(productService.save(any(ProductDTO.class))).thenAnswer(invocationOnMock -> {
            ProductDTO productDTO = invocationOnMock.getArgument(0);
            productDTO.setProductUrl(PRODUCTS_BASE_URL + "/" + PRODUCT_ID);
            return productDTO;
        });

        //when
        final ResultActions resultActions = mockMvc.perform(post(PRODUCTS_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)));

        //then
        resultActions.andExpect(status().isCreated());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        ProductDTO savedProduct = objectMapper.readValue(response, ProductDTO.class);
        assertNotNull("Product is null", savedProduct);
        assertEquals("Product name is not matching", product1.getName(), savedProduct.getName());
        assertNotNull("Product URL is null", savedProduct.getProductUrl());
    }

    @Test
    public void updateProduct() throws Exception {
        final String newName = "New First Name";
        //Given
        ProductDTO product1 = new ProductDTO();
        product1.setName(newName);
        ObjectMapper objectMapper = new ObjectMapper();

        when(productService.updateProduct(anyLong(), any(ProductDTO.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(1));

        //when
        final ResultActions resultActions = mockMvc.perform(patch(PRODUCTS_BASE_URL + "/" + PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)));

        //then
        resultActions.andExpect(status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        ProductDTO productDTO = objectMapper.readValue(response, ProductDTO.class);
        assertNotNull("Product is null", productDTO);
        assertEquals("Product name is not matching", newName, productDTO.getName());
    }

    @Test
    public void replaceProduct() throws Exception {
        //Given
        ProductDTO product1 = new ProductDTO();
        product1.setName(PRODUCT_NAME);
        ObjectMapper objectMapper = new ObjectMapper();

        when(productService.updateProduct(anyLong(), any(ProductDTO.class))).thenReturn(product1);

        //when
        final ResultActions resultActions = mockMvc.perform(patch(PRODUCTS_BASE_URL + "/" + PRODUCT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)));

        //then
        resultActions.andExpect(status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        ProductDTO savedProduct = objectMapper.readValue(response, ProductDTO.class);
        assertNotNull("Product is null", savedProduct);
        assertEquals("Product Name is not matching", PRODUCT_NAME, savedProduct.getName());
    }

    @Test
    public void deleteProduct() throws Exception {
        mockMvc.perform(get(PRODUCTS_BASE_URL + "/delete/1")).andExpect(status().isOk());
    }


    @Test
    public void deleteProduct_InValidId() throws Exception {
        doThrow(new NotFoundException("No Product Found for given id")).when(productService).deleteById(anyLong());
        mockMvc.perform(get(PRODUCTS_BASE_URL + "/delete/1")).andExpect(status().isNotFound());
    }
}