package org.venkat.freshfruits.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.venkat.freshfruits.entity.Vendor;

@Repository
public interface VendorRepository extends CrudRepository<Vendor, Long> {

}
