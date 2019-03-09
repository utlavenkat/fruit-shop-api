package org.venkat.freshfruits.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.venkat.freshfruits.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}
