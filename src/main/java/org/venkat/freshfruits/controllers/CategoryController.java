package org.venkat.freshfruits.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.venkat.freshfruits.dto.CategoriesDataDTO;
import org.venkat.freshfruits.dto.CategoryDTO;
import org.venkat.freshfruits.services.CategoryService;

@RestController
@RequestMapping("/shop/categories")
@RequiredArgsConstructor
@Slf4j
@Api(value = "Fruit Categories", description = "REST API for Fruit Categories", tags = {"Fruit Categories"})
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get All Fruit Categories")
    public CategoriesDataDTO getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a Fruit Category by name")
    public CategoryDTO getCategory(@PathVariable String name) {
        log.info("Name::" + name);
        return categoryService.findCategoryByName(name);
    }

}
