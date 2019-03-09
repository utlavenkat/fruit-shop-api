package org.venkat.freshfruits.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDTO {

    private String name;

    @JsonProperty("product_url")
    private String productUrl;
}
