package org.venkat.freshfruits.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.venkat.freshfruits.dto.CustomerDTO;
import org.venkat.freshfruits.dto.CustomersListDataDTO;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.services.CustomerService;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    private static final String CUSTOMERS_BASE_URL = "/shop/customers";
    private static final String FIRST_NAME = "Test First Name";
    private static final String LAST_NAME = "Test Last Name";
    private static final Long CUSTOMER_ID = 100L;


    @Mock
    private CustomerService customerService;

    private MockMvc mockMvc;
    private CustomerController customerController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerController = new CustomerController(customerService);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).setControllerAdvice(new ExceptionControllerAdvise()).build();
    }

    @Test
    public void getAllCustomers() throws Exception {
        //Given
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setId(CUSTOMER_ID);
        customer1.setFirstName(FIRST_NAME);
        customer1.setLastName(LAST_NAME);
        customer1.setCustomerUrl(CUSTOMERS_BASE_URL + "/" + CUSTOMER_ID);

        when(customerService.findAllCustomers()).thenReturn(new CustomersListDataDTO(Collections.singletonList(customer1)));

        //when
        final ResultActions resultActions = mockMvc.perform(get(CUSTOMERS_BASE_URL));

        //then
        resultActions.andExpect(status().isOk());
        MvcResult mvcResult = resultActions.andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        CustomersListDataDTO customersListDataDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomersListDataDTO.class);

        assertNotNull("Response is null", customersListDataDTO);
        assertEquals("Incorrect results returns", 1, customersListDataDTO.getCustomers().size());
    }

    @Test
    public void createCustomer() throws Exception {
        //Given
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setFirstName(FIRST_NAME);
        customer1.setLastName(LAST_NAME);
        ObjectMapper objectMapper = new ObjectMapper();

        when(customerService.save(any(CustomerDTO.class))).thenAnswer(invocationOnMock -> {
            CustomerDTO customerDTO = invocationOnMock.getArgument(0);
            customerDTO.setId(CUSTOMER_ID);
            customerDTO.setCustomerUrl(CUSTOMERS_BASE_URL + "/" + CUSTOMER_ID);
            return customerDTO;
        });

        //when
        final ResultActions resultActions = mockMvc.perform(post(CUSTOMERS_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer1)));

        //then
        resultActions.andExpect(status().isCreated());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        CustomerDTO savedCustomer = objectMapper.readValue(response, CustomerDTO.class);
        assertNotNull("Customer is null", savedCustomer);
        assertNotNull("Customer id is null", savedCustomer.getId());
        assertEquals("First name is not matching", customer1.getFirstName(), savedCustomer.getFirstName());
        assertEquals("Last name is not matching", customer1.getLastName(), savedCustomer.getLastName());
        assertNotNull("Customer URL is null", savedCustomer.getCustomerUrl());
    }

    @Test
    public void deleteCustomer_validId() throws Exception {
        mockMvc.perform(get(CUSTOMERS_BASE_URL + "/delete/1")).andExpect(status().isOk());
    }

    @Test
    public void deleteCustomer_InValidId() throws Exception {
        doThrow(new NotFoundException("No Customer Found for given id")).when(customerService).deleteById(anyLong());
        mockMvc.perform(get(CUSTOMERS_BASE_URL + "/delete/1")).andExpect(status().isNotFound());
    }

    @Test
    public void getCustomerById() throws Exception {
        //Given
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setId(CUSTOMER_ID);
        customer1.setFirstName(FIRST_NAME);
        customer1.setLastName(LAST_NAME);
        when(customerService.findById(anyLong())).thenReturn(customer1);

        //when
        ResultActions resultActions = mockMvc.perform(get(CUSTOMERS_BASE_URL + "/1"));

        //then
        resultActions.andExpect(status().isOk());
        ObjectMapper objectMapper = new ObjectMapper();

        CustomerDTO responseCustomerDTO = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString()
                , CustomerDTO.class);
        assertNotNull("Customer is null", responseCustomerDTO);
        assertEquals("Customer Id is not matching", CUSTOMER_ID, responseCustomerDTO.getId());
    }

    @Test
    public void getCustomerById_InvalidId() throws Exception {
        //Given

        when(customerService.findById(anyLong())).thenThrow(new NotFoundException("No Customer found for the given id"));

        //when
        ResultActions resultActions = mockMvc.perform(get(CUSTOMERS_BASE_URL + "/1"));

        //then
        resultActions.andExpect(status().isNotFound());

    }

    @Test
    public void patchCustomer() throws Exception {
        final String newName = "New First Name";
        //Given
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setFirstName(newName);
        ObjectMapper objectMapper = new ObjectMapper();

        when(customerService.updateCustomer(anyLong(), any(CustomerDTO.class))).thenAnswer(invocationOnMock -> {
            CustomerDTO customerDTO = invocationOnMock.getArgument(1);
            customerDTO.setId(CUSTOMER_ID);
            customerDTO.setLastName(LAST_NAME);
            return customerDTO;
        });

        //when
        final ResultActions resultActions = mockMvc.perform(patch(CUSTOMERS_BASE_URL + "/" + CUSTOMER_ID).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer1)));

        //then
        resultActions.andExpect(status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("Response received::" + response);
        CustomerDTO savedCustomer = objectMapper.readValue(response, CustomerDTO.class);
        assertNotNull("Customer is null", savedCustomer);
        assertNotNull("Customer id is null", savedCustomer.getId());
        assertEquals("First name is not matching", newName, savedCustomer.getFirstName());
        assertEquals("Last name is not matching", LAST_NAME, savedCustomer.getLastName());
    }

    @Test
    public void updateCustomer() throws Exception {
        //Given
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setFirstName(FIRST_NAME);
        customer1.setLastName(LAST_NAME);
        customer1.setId(CUSTOMER_ID);
        ObjectMapper objectMapper = new ObjectMapper();

        when(customerService.updateCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(customer1);

        //when
        final ResultActions resultActions = mockMvc.perform(patch(CUSTOMERS_BASE_URL + "/" + CUSTOMER_ID).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer1)));

        //then
        resultActions.andExpect(status().isOk());
        String response = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("Response received::" + response);
        CustomerDTO savedCustomer = objectMapper.readValue(response, CustomerDTO.class);
        assertNotNull("Customer is null", savedCustomer);
        assertNotNull("Customer id is null", savedCustomer.getId());
        assertEquals("First name is not matching", FIRST_NAME, savedCustomer.getFirstName());
        assertEquals("Last name is not matching", LAST_NAME, savedCustomer.getLastName());
    }


}
