package org.venkat.freshfruits.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.venkat.freshfruits.dto.CustomerDTO;
import org.venkat.freshfruits.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO entityToDto(Customer customer);

    Customer dtoToEntity(CustomerDTO customerDTO);
}
