package org.venkat.freshfruits.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.venkat.freshfruits.dto.ProductDTO;
import org.venkat.freshfruits.dto.ProductsListDataDTO;
import org.venkat.freshfruits.entity.Product;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.mappers.ProductMapper;
import org.venkat.freshfruits.repositories.ProductRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    private static final String PRODUCT_NAME = "Test PRODUCT";
    private static final Long PRODUCT_ID = 100L;

    @Mock
    private ProductRepository productRepository;

    private ProductMapper productMapper = ProductMapper.productMapper;

    private ProductService productService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productService = new ProductService(productRepository, productMapper);
    }

    @Test
    public void findAllProducts() {
        //given
        Product product1 = new Product();
        product1.setName(PRODUCT_NAME);
        product1.setId(PRODUCT_ID);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(PRODUCT_NAME);

        when(productRepository.findAll()).thenReturn(Collections.singleton(product1));

        //when
        final ProductsListDataDTO productsListDataDTO = productService.findAllProducts();

        //then
        assertNotNull("Product Data is null", productsListDataDTO);
        assertEquals("Count is not matching", 1, productsListDataDTO.getProducts().size());
        ProductDTO savedProductDto = productsListDataDTO.getProducts().get(0);
        assertNotNull("Product URL is null", savedProductDto.getProductUrl());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void save() {
        //Given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(PRODUCT_NAME);

        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setId(PRODUCT_ID);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        //when
        ProductDTO savedProductDto = productService.save(productDTO);

        //then
        assertNotNull("Saved Product is null", savedProductDto);
        assertEquals("Product name is not matching", PRODUCT_NAME, savedProductDto.getName());
        assertNotNull("Product URL is null", savedProductDto.getProductUrl());
    }

    @Test
    public void deleteById() {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setId(PRODUCT_ID);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        //when
        productService.deleteById(PRODUCT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteById_Invalid() {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setId(PRODUCT_ID);

        when(productRepository.findById(anyLong())).thenThrow(new NotFoundException("No Product found for the given id"));

        //when
        productService.deleteById(PRODUCT_ID);
    }

    @Test
    public void findById() {
        //Given
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(PRODUCT_NAME);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        //when
        ProductDTO productDTO = productService.findById(PRODUCT_ID);

        //then
        assertNotNull("Product is null", productDTO);
        assertEquals("Name not matching", PRODUCT_NAME, productDTO.getName());
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void findById_InvalidId() {
        //Given
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(PRODUCT_NAME);

        when(productRepository.findById(anyLong())).thenThrow(new NotFoundException("Product not found for the given id"));

        //when
        productService.findById(PRODUCT_ID);
    }

    @Test
    public void replaceProduct() {
        //Given
        final String updated_name = "Updated Product";
        Product savedProduct = new Product();
        savedProduct.setId(PRODUCT_ID);
        savedProduct.setName(PRODUCT_NAME);

        ProductDTO newProductDetails = new ProductDTO();
        newProductDetails.setName(updated_name);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(savedProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //when
        ProductDTO replacedProduct = productService.replaceProduct(PRODUCT_ID, newProductDetails);

        //then
        assertNotNull("Product is null", replacedProduct);
        assertEquals("Product Name not matching", updated_name, replacedProduct.getName());
    }

    @Test(expected = NotFoundException.class)
    public void replaceProduct_InvalidId() {
        //Given
        final String updated_name = "Updated Product";
        Product savedProduct = new Product();
        savedProduct.setId(PRODUCT_ID);
        savedProduct.setName(PRODUCT_NAME);

        ProductDTO newProductDetails = new ProductDTO();
        newProductDetails.setName(updated_name);

        when(productRepository.findById(anyLong())).thenThrow(new NotFoundException("No Product found for the given id"));
        when(productRepository.save(any(Product.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //when
        productService.replaceProduct(PRODUCT_ID, newProductDetails);

    }

    @Test
    public void updateProduct() {
        //Given
        final String updated_name = "Updated Product";
        Product savedProduct = new Product();
        savedProduct.setId(PRODUCT_ID);
        savedProduct.setName(PRODUCT_NAME);

        ProductDTO newProductDetails = new ProductDTO();
        newProductDetails.setName(updated_name);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(savedProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //when
        ProductDTO replacedProduct = productService.replaceProduct(PRODUCT_ID, newProductDetails);

        //then
        assertNotNull("Product is null", replacedProduct);
        assertEquals("Product Name not matching", updated_name, replacedProduct.getName());

    }

    @Test(expected = NotFoundException.class)
    public void updateProduct_InvalidId() {
        //Given
        final String updated_name = "Updated Product";
        Product savedProduct = new Product();
        savedProduct.setId(PRODUCT_ID);
        savedProduct.setName(PRODUCT_NAME);

        ProductDTO newProductDetails = new ProductDTO();
        newProductDetails.setName(updated_name);

        when(productRepository.findById(anyLong())).thenThrow(new NotFoundException("No Product found for the given id"));
        when(productRepository.save(any(Product.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        //when
        productService.updateProduct(PRODUCT_ID, newProductDetails);
    }
}