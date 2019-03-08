package org.venkat.freshfruits.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.venkat.freshfruits.entity.Customer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryIT {

    private static final String FIRST_NAME = "Test First Name";
    private static final String LAST_NAME = "Test Last Name";

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findAllCustomers() {
        List<Customer> customers = StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        assertNotNull("Customers does not exists", customers);
        assertTrue("Customers list is empty", customers.size() > 0);
    }

    @Test
    public void save() {
        //Given
        final Customer customer = new Customer();
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);

        //when
        Customer savedCustomer = customerRepository.save(customer);

        //then
        assertNotNull("Customer is not saved", savedCustomer);
        assertEquals("First Name not matching", FIRST_NAME, savedCustomer.getFirstName());
        assertEquals("Last Name not matching", LAST_NAME, savedCustomer.getLastName());
        assertTrue("Customer ID not generated", savedCustomer.getId() > 0);
    }

    @Test
    public void delete() {
        //Given
        final Customer customer = new Customer();
        customer.setFirstName("Delete First");
        customer.setLastName("Delete Last");
        Customer savedCustomer = customerRepository.save(customer);
        assertTrue("Customer ID not generated", savedCustomer.getId() > 0);

        //when
        customerRepository.deleteById(savedCustomer.getId());

        //then
        Optional<Customer> optionalCustomer = customerRepository.findById(savedCustomer.getId());
        assertFalse("Customer not deleted", optionalCustomer.isPresent());
    }

    @Test
    public void findCustomerById() {
        //Given
        Customer existingCustomer = customerRepository.findAll().iterator().next();

        //when
        Optional<Customer> customerInTest = customerRepository.findById(existingCustomer.getId());

        //then
        assertTrue("Customer not found", customerInTest.isPresent());
        assertEquals("Customer id not matching", existingCustomer.getId(), customerInTest.get().getId());
    }

    @Test
    public void updateCustomer() {
        final String firstName = "NEW_FIRST_NAME";
        final String lastName = "NEW_LAST_NAME";
        //Given
        Customer existingCustomer = customerRepository.findAll().iterator().next();
        existingCustomer.setFirstName(firstName);
        existingCustomer.setLastName(lastName);

        //when
        Customer updatedCustomer = customerRepository.save(existingCustomer);

        //then
        assertNotNull("Customer is null", updatedCustomer);
        assertEquals("First Name is not matching", firstName, updatedCustomer.getFirstName());
        assertEquals("Last Name is not matching", lastName, updatedCustomer.getLastName());
        assertEquals("Customer ID is not matching", existingCustomer.getId(), updatedCustomer.getId());
    }

}