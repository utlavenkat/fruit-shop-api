package org.venkat.freshfruits.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.venkat.freshfruits.dto.CategoriesDataDTO;
import org.venkat.freshfruits.dto.CategoryDTO;
import org.venkat.freshfruits.entity.Category;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.mappers.CategoryMapper;
import org.venkat.freshfruits.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoriesDataDTO getAllCategories() {
        List<Category> categoryList = StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));
        List<CategoryDTO> categoryDTOList = categoryList.stream()
                .map(category ->
                        {
                            CategoryDTO categoryDTO = categoryMapper.entityToDto(category);
                            categoryDTO.setCategoryUrl("/shop/categories/" + category.getName());
                            return categoryDTO;
                        }
                )
                .collect(Collectors.toCollection(ArrayList::new));
        return new CategoriesDataDTO(categoryDTOList);
    }

    public CategoryDTO findCategoryByName(final String name) {
        final Category category = categoryRepository.findByNameIgnoreCase(name);
        if (category == null) {
            throw new NotFoundException("There is no category with the name '" + name + "'");
        }
        return categoryMapper.entityToDto(category);
    }
}
