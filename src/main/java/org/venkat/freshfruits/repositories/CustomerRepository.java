package org.venkat.freshfruits.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.venkat.freshfruits.entity.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
}
