package org.venkat.freshfruits.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.venkat.freshfruits.entity.Category;
import org.venkat.freshfruits.entity.Customer;
import org.venkat.freshfruits.entity.Product;
import org.venkat.freshfruits.entity.Vendor;
import org.venkat.freshfruits.repositories.CategoryRepository;
import org.venkat.freshfruits.repositories.CustomerRepository;
import org.venkat.freshfruits.repositories.ProductRepository;
import org.venkat.freshfruits.repositories.VendorRepository;

@Component
@RequiredArgsConstructor
public class DefaultDataLoader implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        loadCategories();
        loadCustomers();
        loadVendors();
        loadProducts();
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

    private void loadVendors() {
        Iterable<Vendor> vendors = vendorRepository.findAll();
        if (vendors == null || !vendors.iterator().hasNext()) {
            Vendor vendor1 = new Vendor();
            vendor1.setName("Venkatasubbaiah");
            vendorRepository.save(vendor1);

            Vendor vendor2 = new Vendor();
            vendor2.setName("Vendor2");
            vendorRepository.save(vendor2);

            Vendor vendor3 = new Vendor();
            vendor3.setName("Vendor3");
            vendorRepository.save(vendor3);

            Vendor vendor4 = new Vendor();
            vendor4.setName("Vendor4");
            vendorRepository.save(vendor4);

        }
    }

    private void loadProducts() {
        Iterable<Product> products = productRepository.findAll();
        if (products == null || !products.iterator().hasNext()) {

            Product bananas = new Product();
            bananas.setName("Bananas");
            productRepository.save(bananas);

            Product oranges = new Product();
            oranges.setName("Oranges");
            productRepository.save(oranges);

            Product pineapples = new Product();
            pineapples.setName("Pineapples");
            productRepository.save(pineapples);

        }
    }

}
