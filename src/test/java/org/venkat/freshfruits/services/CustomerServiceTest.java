package org.venkat.freshfruits.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.venkat.freshfruits.dto.CustomerDTO;
import org.venkat.freshfruits.dto.CustomersListDataDTO;
import org.venkat.freshfruits.entity.Customer;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.mappers.CustomerMapper;
import org.venkat.freshfruits.repositories.CustomerRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class CustomerServiceTest {

    private static final String FIRST_NAME = "Test First Name";
    private static final String LAST_NAME = "Test Last Name";
    private static final Long CUSTOMER_ID = 100L;

    @Mock
    private CustomerRepository customerRepository;

    private CustomerMapper customerMapper = CustomerMapper.customerMapper;

    private CustomerService customerService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerService(customerRepository, customerMapper);
    }

    @Test
    public void findAllCustomers() {
        //Given
        Customer customer1 = new Customer();
        customer1.setFirstName(FIRST_NAME);
        customer1.setLastName(LAST_NAME);
        customer1.setId(CUSTOMER_ID);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(FIRST_NAME);
        customerDTO.setLastName(LAST_NAME);
        customerDTO.setId(CUSTOMER_ID);

        when(customerRepository.findAll()).thenReturn(Collections.singleton(customer1));
        //when
        final CustomersListDataDTO customersListDataDTO = customerService.findAllCustomers();

        //then
        assertNotNull("Customer Data is null", customersListDataDTO);
        assertEquals("Count is not matching", 1, customersListDataDTO.getCustomers().size());
        CustomerDTO savedCustomerDTO = customersListDataDTO.getCustomers().get(0);
        assertNotNull("Customer URL is null", savedCustomerDTO.getCustomerUrl());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void save() {
        //Given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setLastName(LAST_NAME);
        customerDTO.setFirstName(FIRST_NAME);
        customerDTO.setId(CUSTOMER_ID);

        Customer customer = new Customer();
        customer.setLastName(LAST_NAME);
        customer.setFirstName(FIRST_NAME);
        customer.setId(CUSTOMER_ID);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        //when
        CustomerDTO savedCustomerDTO = customerService.save(customerDTO);
        //then
        assertNotNull("Saved customer is null", savedCustomerDTO);
        assertEquals("Customer ID is not matching", CUSTOMER_ID, savedCustomerDTO.getId());
        assertEquals("Customer First Name is not matching", FIRST_NAME, savedCustomerDTO.getFirstName());
        assertEquals("Customer Last name is not mactching", LAST_NAME, savedCustomerDTO.getLastName());
    }

    @Test
    public void deleteById() {
        Customer customer = new Customer();
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        customer.setId(CUSTOMER_ID);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        //when
        customerService.deleteById(CUSTOMER_ID);
        //then
    }

    @Test(expected = NotFoundException.class)
    public void deleteById_Invalid() {

        when(customerRepository.findById(anyLong())).thenThrow(new NotFoundException("No customer found for the given id "));
        //when
        customerService.deleteById(CUSTOMER_ID);
        //then
    }

    @Test
    public void findById() {
        //Given
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setLastName(LAST_NAME);
        customer.setFirstName(FIRST_NAME);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        //when
        CustomerDTO customerDTO = customerService.findById(CUSTOMER_ID);
        //then
        assertNotNull("Customer is null", customerDTO);
        assertEquals("First Name not matching", FIRST_NAME, customerDTO.getFirstName());
        assertEquals("Last Name is not matching", LAST_NAME, customerDTO.getLastName());
        assertEquals("ID is not matching", CUSTOMER_ID, customerDTO.getId());
        assertNull("customer url is not null", customerDTO.getCustomerUrl());

        verify(customerRepository, times(1)).findById(anyLong());

    }

    @Test(expected = NotFoundException.class)
    public void findById_Invalid() {
        //Given
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setLastName(LAST_NAME);
        customer.setFirstName(FIRST_NAME);
        when(customerRepository.findById(anyLong())).thenThrow(new NotFoundException("Customer not found for the given id"));
        //when
        customerService.findById(CUSTOMER_ID);

    }

    @Test
    public void replaceCustomer() {
        //Given
        final String updated_first_name = "Updated First Name";
        final String updated_last_name = "Updated Last Name";

        Customer savedCustomer = new Customer();
        savedCustomer.setId(CUSTOMER_ID);
        savedCustomer.setFirstName(FIRST_NAME);
        savedCustomer.setLastName(LAST_NAME);

        CustomerDTO newCustomerDetails = new CustomerDTO();
        newCustomerDetails.setFirstName(updated_first_name);
        newCustomerDetails.setLastName(updated_last_name);

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocationOnMock -> invocationOnMock.<Customer>getArgument(0));

        //when
        CustomerDTO replacedCustomer = customerService.replaceCustomer(CUSTOMER_ID, newCustomerDetails);
        //then
        assertNotNull("Customer is null", replacedCustomer);
        assertEquals("Customer ID not matching", CUSTOMER_ID, replacedCustomer.getId());
        assertEquals("First Name not matching", updated_first_name, replacedCustomer.getFirstName());
        assertEquals("Last name not matching", updated_last_name, replacedCustomer.getLastName());

    }

    @Test
    public void updateCustomer() {
        final String updated_first_name = "Updated First Name";
        //Given
        Customer savedCustomer = new Customer();
        savedCustomer.setId(CUSTOMER_ID);
        savedCustomer.setFirstName(FIRST_NAME);
        savedCustomer.setLastName(LAST_NAME);

        CustomerDTO newCustomerDetails = new CustomerDTO();
        newCustomerDetails.setFirstName(updated_first_name);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(savedCustomer));

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocationOnMock -> invocationOnMock.<Customer>getArgument(0));
        //when
        CustomerDTO updatedCustomer = customerService.updateCustomer(CUSTOMER_ID, newCustomerDetails);
        //then
        assertNotNull("Customer is null", updatedCustomer);
        assertEquals("Customer ID not matching", CUSTOMER_ID, updatedCustomer.getId());
        assertEquals("First Name not matching", updated_first_name, updatedCustomer.getFirstName());
        assertEquals("Last name not matching", LAST_NAME, updatedCustomer.getLastName());

    }

    @Test(expected = NotFoundException.class)
    public void updateCustomer_InvalidID() {
        final String updated_first_name = "Updated First Name";
        //Given
        Customer savedCustomer = new Customer();
        savedCustomer.setId(CUSTOMER_ID);
        savedCustomer.setFirstName(FIRST_NAME);
        savedCustomer.setLastName(LAST_NAME);

        CustomerDTO newCustomerDetails = new CustomerDTO();
        newCustomerDetails.setFirstName(updated_first_name);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocationOnMock -> invocationOnMock.<Customer>getArgument(0));

        //when
        customerService.updateCustomer(CUSTOMER_ID, newCustomerDetails);
    }
}