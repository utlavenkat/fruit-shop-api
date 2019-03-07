package org.venkat.freshfruits.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.venkat.freshfruits.entity.Category;
import org.venkat.freshfruits.entity.Customer;
import org.venkat.freshfruits.repositories.CategoryRepository;
import org.venkat.freshfruits.repositories.CustomerRepository;

@Component
@RequiredArgsConstructor
public class DefaultDataLoader implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        loadCategories();
        loadCustomers();
    }

    private void loadCategories() {
        Iterable<Category> categories = categoryRepository.findAll();
        if (categories == null || !categories.iterator().hasNext()) {

            Category fruits = new Category();
            fruits.setName("Fruits");
            categoryRepository.save(fruits);

            Category dried = new Category();
            dried.setName("Dried");
            categoryRepository.save(dried);

            Category fresh = new Category();
            fresh.setName("Fresh");
            categoryRepository.save(fresh);

            Category exotic = new Category();
            exotic.setName("Exotic");
            categoryRepository.save(exotic);

            Category nuts = new Category();
            nuts.setName("Nuts");
            categoryRepository.save(nuts);
        }
    }


    private void loadCustomers() {
        Iterable<Customer> customers = customerRepository.findAll();
        if (customers == null || !customers.iterator().hasNext()) {
            Customer venkat = new Customer();
            venkat.setFirstName("Venkatasubbaiah");
            venkat.setLastName("Utla");
            customerRepository.save(venkat);

            Customer lakshmi = new Customer();
            lakshmi.setFirstName("Lakshmi");
            lakshmi.setLastName("Utla");
            customerRepository.save(lakshmi);

            Customer hanshitha = new Customer();
            hanshitha.setFirstName("Hanshitha");
            hanshitha.setLastName("Utla");
            customerRepository.save(hanshitha);

            Customer praneel = new Customer();
            praneel.setFirstName("Praneel");
            praneel.setLastName("Utla");
            customerRepository.save(praneel);
        }
    }
}
