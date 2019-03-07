package org.venkat.freshfruits.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CustomersListDataDTO {

    private List<CustomerDTO> customers;
}
