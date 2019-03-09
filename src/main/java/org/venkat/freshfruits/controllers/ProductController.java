package org.venkat.freshfruits.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.venkat.freshfruits.dto.ProductDTO;
import org.venkat.freshfruits.dto.ProductsListDataDTO;
import org.venkat.freshfruits.services.ProductService;

@RestController
@RequiredArgsConstructor
@Api(value = "Products", description = "REST API for Product", tags = {"Products"})
public class ProductController {
    private final ProductService productService;

    @GetMapping("/shop/products")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Lists all the Products")
    public ProductsListDataDTO getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/shop/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a product by id")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping("/shop/products")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a product")
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productService.save(productDTO);
    }

    @PatchMapping("/shop/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update one of more properties of product")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @PutMapping("/shop/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Replace a product by new data")
    public ProductDTO replaceProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.replaceProduct(id, productDTO);
    }

    @GetMapping("/shop/products/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete a product")
    public void deleteProduct(@PathVariable final Long id) {
        productService.deleteById(id);
    }
}
