package org.venkat.freshfruits.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.venkat.freshfruits.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositoryTest {
    private static final String PRODUCT_NAME = "Test Product";

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void findAllProducts() {
        List<Product> products = StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        assertNotNull("Products does not exists", products);
        assertTrue("Product list is empty", products.size() > 0);
    }

    @Test
    public void save() {
        //Given
        final Product product = new Product();
        product.setName(PRODUCT_NAME);

        //when
        Product savedProduct = productRepository.save(product);

        //then
        assertNotNull("Product is not saved", savedProduct);
        assertEquals("Product Name not matching", PRODUCT_NAME, savedProduct.getName());
        assertTrue("Product ID not generated", product.getId() > 0);
    }

    @Test
    public void delete() {
        //Given
        final Product product = new Product();
        product.setName("Delete Name");
        Product savedProduct = productRepository.save(product);
        assertTrue("Product ID not generated", product.getId() > 0);

        //when
        productRepository.deleteById(savedProduct.getId());

        //then
        Optional<Product> optionalProduct = productRepository.findById(savedProduct.getId());
        assertFalse("Product not deleted", optionalProduct.isPresent());
    }

    @Test
    public void findProductById() {
        //Given
        Product existingProduct = productRepository.findAll().iterator().next();

        //when
        Optional<Product> productInTest = productRepository.findById(existingProduct.getId());

        //then
        assertTrue("Product not found", productInTest.isPresent());
        assertEquals("Product id not matching", existingProduct.getId(), productInTest.get().getId());
    }

    @Test
    public void updateProduct() {
        final String name = "NEW__NAME";

        //Given
        Product existingProduct = productRepository.findAll().iterator().next();
        existingProduct.setName(name);

        //when
        Product updatedProduct = productRepository.save(existingProduct);

        //then
        assertNotNull("Product is null", updatedProduct);
        assertEquals("Product Name is not matching", name, updatedProduct.getName());
        assertEquals("Product ID is not matching", existingProduct.getId(), updatedProduct.getId());
    }

}