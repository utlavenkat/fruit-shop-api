package org.venkat.freshfruits.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.venkat.freshfruits.dto.CustomerDTO;
import org.venkat.freshfruits.dto.CustomersListDataDTO;
import org.venkat.freshfruits.entity.Customer;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.mappers.CustomerMapper;
import org.venkat.freshfruits.repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final String CUSTOMER_BASE_URL = "/shop/customer/";

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomersListDataDTO findAllCustomers() {

        List<Customer> customers = StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return new CustomersListDataDTO(
                customers.stream().map(customer -> {
                    CustomerDTO customerDTO = customerMapper.entityToDto(customer);
                    customerDTO.setCustomerUrl(CUSTOMER_BASE_URL + customerDTO.getId());
                    return customerDTO;
                }).collect(Collectors.toList()));
    }

    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer savedCustomer = customerRepository.save(customerMapper.dtoToEntity(customerDTO));
        return customerMapper.entityToDto(savedCustomer);
    }

    public void deleteById(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            throw new NotFoundException("No Customer Found for given id " + id);
        }
        customerRepository.deleteById(id);
    }

    public CustomerDTO findById(Long id) {

        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            throw new NotFoundException("No Customer found for the Id: " + id);
        }
        return customerMapper.entityToDto(customer);
    }

    public CustomerDTO replaceCustomer(Long id, CustomerDTO customerDTO) {
        customerDTO.setId(id);
        Customer savedCustomer = customerRepository.save(customerMapper.dtoToEntity(customerDTO));
        return customerMapper.entityToDto(savedCustomer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new NotFoundException("No customer found for the given id " + id);
        }
        Customer customer = optionalCustomer.get();
        if (!StringUtils.isEmpty(customerDTO.getFirstName())) {
            customer.setFirstName(customerDTO.getFirstName());
        }
        if (!StringUtils.isEmpty(customerDTO.getLastName())) {
            customer.setLastName(customerDTO.getLastName());
        }
        return customerMapper.entityToDto(customerRepository.save(customer));
    }


}
