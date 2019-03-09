package org.venkat.freshfruits.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.venkat.freshfruits.dto.ProductDTO;
import org.venkat.freshfruits.dto.ProductsListDataDTO;
import org.venkat.freshfruits.entity.Product;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.mappers.ProductMapper;
import org.venkat.freshfruits.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final String PRODUCTS_BASE_URL = "/shop/vendors/";
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductsListDataDTO findAllProducts() {
        List<Product> products = StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return new ProductsListDataDTO(products.stream().map(product -> {
            ProductDTO productDTO = productMapper.entityToDto(product);
            productDTO.setProductUrl(PRODUCTS_BASE_URL + product.getId());
            return productDTO;
        }).collect(Collectors.toList()));
    }

    public ProductDTO save(ProductDTO productDTO) {
        Product savedProduct = productRepository.save(productMapper.dtoToEntity(productDTO));
        productDTO = productMapper.entityToDto(savedProduct);
        productDTO.setProductUrl(PRODUCTS_BASE_URL + savedProduct.getId());
        return productDTO;
    }

    public void deleteById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new NotFoundException("No Product found for given id " + id);
        }
        productRepository.deleteById(id);
    }

    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new NotFoundException("No Product found for the Id " + id);
        }
        return productMapper.entityToDto(product);
    }

    public ProductDTO replaceProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new NotFoundException("No Product found for the Id " + id);
        }
        product = productMapper.dtoToEntity(productDTO);
        product.setId(id);
        Product savedProduct = productRepository.save(product);
        return productMapper.entityToDto(savedProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new NotFoundException("No Product found for the Id " + id);
        }
        if (!StringUtils.isEmpty(product.getName())) {
            product.setName(productDTO.getName());
        }
        return productMapper.entityToDto(productRepository.save(product));
    }

}
