package org.venkat.freshfruits.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.venkat.freshfruits.entity.Category;
import org.venkat.freshfruits.repositories.CategoryRepository;

@Component
@RequiredArgsConstructor
public class DefaultDataLoader implements CommandLineRunner {
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        loadCategories();
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
}
