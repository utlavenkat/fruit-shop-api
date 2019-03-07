package org.venkat.freshfruits.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;

    @JsonProperty("customer_url")
    private String customerUrl;
}
