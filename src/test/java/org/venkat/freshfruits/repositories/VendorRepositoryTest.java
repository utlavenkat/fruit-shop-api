package org.venkat.freshfruits.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.venkat.freshfruits.entity.Vendor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VendorRepositoryTest {

    private static final String VENDOR_NAME = "Hanshitha Heritage";

    @Autowired
    private VendorRepository vendorRepository;

    @Test
    public void findAllVendors() {
        List<Vendor> vendors = StreamSupport.stream(vendorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        assertNotNull("Vendors does not exists", vendors);
        assertTrue("Vendors list is empty", vendors.size() > 0);
    }

    @Test
    public void save() {
        //Given
        final Vendor vendor = new Vendor();
        vendor.setName(VENDOR_NAME);

        //when
        Vendor savedVendor = vendorRepository.save(vendor);

        //then
        assertNotNull("Vendor is not saved", savedVendor);
        assertEquals("Vendor Name not matching", VENDOR_NAME, savedVendor.getName());
        assertTrue("Vendor ID not generated", vendor.getId() > 0);
    }

    @Test
    public void delete() {
        //Given
        final Vendor vendor = new Vendor();
        vendor.setName("Delete Name");
        Vendor savedVendor = vendorRepository.save(vendor);
        assertTrue("Vendor ID not generated", vendor.getId() > 0);

        //when
        vendorRepository.deleteById(savedVendor.getId());

        //then
        Optional<Vendor> optionalVendor = vendorRepository.findById(savedVendor.getId());
        assertFalse("Vendor not deleted", optionalVendor.isPresent());
    }

    @Test
    public void findVendorById() {
        //Given
        Vendor existingVendor = vendorRepository.findAll().iterator().next();

        //when
        Optional<Vendor> vendorInTest = vendorRepository.findById(existingVendor.getId());

        //then
        assertTrue("Vendor not found", vendorInTest.isPresent());
        assertEquals("Vendor id not matching", existingVendor.getId(), vendorInTest.get().getId());
    }

    @Test
    public void updateVendor() {
        final String name = "NEW__NAME";

        //Given
        Vendor existingVendor = vendorRepository.findAll().iterator().next();
        existingVendor.setName(name);

        //when
        Vendor updatedVendor = vendorRepository.save(existingVendor);

        //then
        assertNotNull("Vendor is null", updatedVendor);
        assertEquals("Vendor Name is not matching", name, updatedVendor.getName());
        assertEquals("Vendor ID is not matching", existingVendor.getId(), updatedVendor.getId());
    }

}