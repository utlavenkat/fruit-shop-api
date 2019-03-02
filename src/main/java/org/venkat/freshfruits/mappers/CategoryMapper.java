package org.venkat.freshfruits.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.venkat.freshfruits.dto.CategoryDTO;
import org.venkat.freshfruits.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper CATEGORY_MAPPER = Mappers.getMapper(CategoryMapper.class);

    Category dtoToEntity(CategoryDTO categoryDTO);

    CategoryDTO entityToDto(Category category);
}
