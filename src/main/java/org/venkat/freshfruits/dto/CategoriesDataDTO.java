package org.venkat.freshfruits.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesDataDTO {
    private List<CategoryDTO> categories;
}
