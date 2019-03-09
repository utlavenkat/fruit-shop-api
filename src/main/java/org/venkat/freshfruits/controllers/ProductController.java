package org.venkat.freshfruits.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.venkat.freshfruits.dto.ProductDTO;
import org.venkat.freshfruits.dto.ProductsListDataDTO;
import org.venkat.freshfruits.services.ProductService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@Api(value = "Products", description = "REST API for Product", tags = {"Products"})
@Slf4j
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

    @PutMapping("/shop/products/{id}/photo")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Add or update the photo of a product")
    public ProductDTO uploadImage(@PathVariable final Long id, @RequestParam("file") MultipartFile file) throws Exception {
        byte[] imageBytes = file.getBytes();
        Byte[] wrapperBytes = new Byte[imageBytes.length];
        int index = 0;
        for (index = 0; index < imageBytes.length; index++) {
            wrapperBytes[index] = imageBytes[index];
        }
        return productService.addOrUpdatePhoto(id, wrapperBytes);
    }

    @GetMapping("/shop/products/{id}/photo")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get photo of a product")
    public void downloadPhoto(@PathVariable final Long id, HttpServletResponse servletResponse) throws IOException {

        Byte[] imageBytes = productService.getPhoto(id);
        servletResponse.setContentType("image/jpeg");
        byte[] byteArray = new byte[imageBytes.length];
        int i = 0;
        for (Byte wrapperByte : imageBytes) {
            byteArray[i++] = wrapperByte;
        }
        InputStream is = new ByteArrayInputStream(byteArray);
        IOUtils.copy(is, servletResponse.getOutputStream());
    }

}
