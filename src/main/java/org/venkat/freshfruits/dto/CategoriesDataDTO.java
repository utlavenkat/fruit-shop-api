package org.venkat.freshfruits.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CategoriesDataDTO {
    private List<CategoryDTO> categories;
}
