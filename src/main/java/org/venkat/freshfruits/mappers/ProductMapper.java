package org.venkat.freshfruits.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.venkat.freshfruits.dto.ProductDTO;
import org.venkat.freshfruits.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    ProductDTO entityToDto(Product product);

    Product dtoToEntity(ProductDTO productDTO);

}
