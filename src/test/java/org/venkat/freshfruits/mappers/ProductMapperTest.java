package org.venkat.freshfruits.mappers;

import org.junit.Test;
import org.venkat.freshfruits.dto.ProductDTO;
import org.venkat.freshfruits.entity.Product;

import static org.junit.Assert.*;

public class ProductMapperTest {

    private static final String PRODUCT_NAME = "Test Product";
    private static final Long PRODUCT_ID = 100L;

    @Test
    public void entityToDto() {
        //Given
        final Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setId(PRODUCT_ID);

        //when
        final ProductDTO productDTO = ProductMapper.productMapper.entityToDto(product);

        //then
        assertNotNull("Product DTO is null", productDTO);
        assertEquals("Product name not matching", PRODUCT_NAME, productDTO.getName());

    }

    @Test
    public void entityToDto_NullTest() {
        //when
        final ProductDTO productDTO = ProductMapper.productMapper.entityToDto(null);

        //then
        assertNull("Customer DTO is not null", productDTO);
    }

    @Test
    public void dtoToEntity() {
        //Given
        final ProductDTO productDTO = new ProductDTO();
        productDTO.setName(PRODUCT_NAME);

        //when
        final Product product = ProductMapper.productMapper.dtoToEntity(productDTO);

        //then
        assertNotNull("Product Entity is null", product);
        assertEquals("Product  name not matching", PRODUCT_NAME, product.getName());
    }

    @Test
    public void dtoToEntity_NullTest() {
        //when
        final Product product = ProductMapper.productMapper.dtoToEntity(null);

        //then
        assertNull("Product Entity is not null", product);
    }
}