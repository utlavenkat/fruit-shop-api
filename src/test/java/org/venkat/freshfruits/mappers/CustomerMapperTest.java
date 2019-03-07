package org.venkat.freshfruits.mappers;

import org.junit.Test;
import org.venkat.freshfruits.dto.CustomerDTO;
import org.venkat.freshfruits.entity.Customer;

import static org.junit.Assert.*;

public class CustomerMapperTest {

    private static final String FIRST_NAME = "VENKAT";
    private static final String LAST_NAME = "UTLA";
    private static final Long CUSTOMER_ID = 1L;

    @Test
    public void entityToDto() {
        //Given
        final Customer customer = new Customer();
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        customer.setId(CUSTOMER_ID);

        //when
        final CustomerDTO customerDTO = CustomerMapper.customerMapper.entityToDto(customer);

        //then
        assertNotNull("Customer DTO is null", customerDTO);
        assertEquals("Customer first name not matching", FIRST_NAME, customerDTO.getFirstName());
        assertEquals("Customer last name not matching", LAST_NAME, customer.getLastName());
        assertEquals("Customer id is not matching", CUSTOMER_ID, customer.getId());
    }

    @Test
    public void entityToDto_NullTest() {
        //when
        final CustomerDTO customerDTO = CustomerMapper.customerMapper.entityToDto(null);

        //then
        assertNull("Customer DTO is not null", customerDTO);
    }

    @Test
    public void dtoToEntity() {
        //Given
        final CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(FIRST_NAME);
        customerDTO.setLastName(LAST_NAME);
        customerDTO.setId(CUSTOMER_ID);

        //when
        final Customer customer = CustomerMapper.customerMapper.dtoToEntity(customerDTO);

        //then
        assertNotNull("Customer Entity is null", customer);
        assertEquals("Customer first name not matching", FIRST_NAME, customer.getFirstName());
        assertEquals("Customer last name not matching", LAST_NAME, customer.getLastName());
        assertEquals("Customer id is not matching", CUSTOMER_ID, customer.getId());
    }

    @Test
    public void dtoToEntity_NullTest() {
        //when
        final Customer customer = CustomerMapper.customerMapper.dtoToEntity(null);

        //then
        assertNull("Customer Entity is not null", customer);
    }
}