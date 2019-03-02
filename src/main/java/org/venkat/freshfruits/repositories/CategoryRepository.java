package org.venkat.freshfruits.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.venkat.freshfruits.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    Category findByNameIgnoreCase(String name);
}
