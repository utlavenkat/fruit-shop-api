package org.venkat.freshfruits.mappers;

import org.junit.Test;
import org.venkat.freshfruits.dto.VendorDTO;
import org.venkat.freshfruits.entity.Vendor;

import static org.junit.Assert.*;

public class VendorMapperTest {

    private static final String VENDOR_NAME = "Hanshitha Heritage";
    private static final Long VENDOR_ID = 100L;

    @Test
    public void entityToDto() {
        //Given
        final Vendor vendor = new Vendor();
        vendor.setName(VENDOR_NAME);
        vendor.setId(VENDOR_ID);

        //when
        final VendorDTO vendorDTO = VendorMapper.vendorMapper.entityToDto(vendor);

        //then
        assertNotNull("Vendor DTO is null", vendorDTO);
        assertEquals("Vendor name not matching", VENDOR_NAME, vendorDTO.getName());

    }

    @Test
    public void entityToDto_NullTest() {
        //when
        final VendorDTO vendorDTO = VendorMapper.vendorMapper.entityToDto(null);

        //then
        assertNull("Customer DTO is not null", vendorDTO);
    }

    @Test
    public void dtoToEntity() {
        //Given
        final VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        //when
        final Vendor vendor = VendorMapper.vendorMapper.dtoToEntity(vendorDTO);

        //then
        assertNotNull("Vendor Entity is null", vendor);
        assertEquals("Vendor  name not matching", VENDOR_NAME, vendor.getName());
    }

    @Test
    public void dtoToEntity_NullTest() {
        //when
        final Vendor vendor = VendorMapper.vendorMapper.dtoToEntity(null);

        //then
        assertNull("Vendor Entity is not null", vendor);
    }
}