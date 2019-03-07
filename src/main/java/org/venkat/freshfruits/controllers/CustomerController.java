package org.venkat.freshfruits.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.venkat.freshfruits.dto.CustomerDTO;
import org.venkat.freshfruits.dto.CustomersListDataDTO;
import org.venkat.freshfruits.services.CustomerService;

@RestController
@RequiredArgsConstructor
@Api(value = "Customers", description = "REST API for Customers", tags = {"Customers"})
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/shop/customers")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Lists all the customers")
    public CustomersListDataDTO getAllCustomers() {
        return customerService.findAllCustomers();
    }

    @GetMapping("/shop/customers/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a Customer by id")
    public CustomerDTO getCustomerById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @PostMapping("/shop/customers")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a customer")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.save(customerDTO);
    }

    @PatchMapping("/shop/customers/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update a customer")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(id, customerDTO);
    }

    @PutMapping("/shop/customers/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Replace a customer")
    public CustomerDTO replaceCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return customerService.replaceCustomer(id, customerDTO);
    }

    @GetMapping("/shop/customers/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete a customer")
    public void deleteCustomer(@PathVariable final Long id) {
        System.out.println("Customer Id::" + id);
        customerService.deleteById(id);
    }


}
