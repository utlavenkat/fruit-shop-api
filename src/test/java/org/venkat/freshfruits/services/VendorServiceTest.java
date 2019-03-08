package org.venkat.freshfruits.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.venkat.freshfruits.dto.VendorDTO;
import org.venkat.freshfruits.dto.VendorsListDataDTO;
import org.venkat.freshfruits.entity.Vendor;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.mappers.VendorMapper;
import org.venkat.freshfruits.repositories.VendorRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VendorServiceTest {

    private static final String VENDOR_NAME = "Hanshitha Heritage";
    private static final Long VENDOR_ID = 100L;

    @Mock
    private VendorRepository vendorRepository;

    private VendorMapper vendorMapper = VendorMapper.vendorMapper;

    private VendorService vendorService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        vendorService = new VendorService(vendorRepository, vendorMapper);
    }

    @Test
    public void findAllVendors() {
        //Given
        Vendor vendor1 = new Vendor();
        vendor1.setName(VENDOR_NAME);
        vendor1.setId(VENDOR_ID);

        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        when(vendorRepository.findAll()).thenReturn(Collections.singleton(vendor1));
        //when
        final VendorsListDataDTO vendorsListDataDTO = vendorService.findAllVendors();

        //then
        assertNotNull("Vendor Data is null", vendorsListDataDTO);
        assertEquals("Count is not matching", 1, vendorsListDataDTO.getVendors().size());
        VendorDTO savedVendorDTO = vendorsListDataDTO.getVendors().get(0);
        assertNotNull("Vendor URL is null", savedVendorDTO.getVendorUrl());
        verify(vendorRepository, times(1)).findAll();
    }

    @Test
    public void save() {
        //Given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        Vendor vendor = new Vendor();
        vendor.setName(VENDOR_NAME);
        vendor.setId(VENDOR_ID);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);
        //when
        VendorDTO savedVendorDTO = vendorService.save(vendorDTO);
        //then
        assertNotNull("Saved Vendor is null", savedVendorDTO);
        assertEquals("Vendor Name is not matching", VENDOR_NAME, savedVendorDTO.getName());
        assertNotNull("Vendor URL is null", savedVendorDTO.getVendorUrl());
    }

    @Test
    public void deleteById() {
        Vendor vendor = new Vendor();
        vendor.setName(VENDOR_NAME);
        vendor.setId(VENDOR_ID);

        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor));
        //when
        vendorService.deleteById(VENDOR_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteById_Invalid() {
        when(vendorRepository.findById(anyLong())).thenThrow(new NotFoundException("No Vendor found for the given id "));
        //when
        vendorService.deleteById(VENDOR_ID);
        //then
    }

    @Test
    public void findById() {
        //Given
        Vendor vendor = new Vendor();
        vendor.setId(VENDOR_ID);
        vendor.setName(VENDOR_NAME);

        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor));
        //when
        VendorDTO vendorDTO = vendorService.findById(VENDOR_ID);
        //then
        assertNotNull("Vendor is null", vendorDTO);
        assertEquals(" Name not matching", VENDOR_NAME, vendorDTO.getName());
        assertNull("Vendor url is not null", vendorDTO.getVendorUrl());

        verify(vendorRepository, times(1)).findById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void findById_Invalid() {
        //Given
        Vendor vendor = new Vendor();
        vendor.setId(VENDOR_ID);
        vendor.setName(VENDOR_NAME);

        when(vendorRepository.findById(anyLong())).thenThrow(new NotFoundException("Vendor not found for the given id"));
        //when
        vendorService.findById(VENDOR_ID);
    }

    @Test
    public void replaceVendor() {
        //Given
        final String updated_name = "Updated  Name";

        Vendor savedVendor = new Vendor();
        savedVendor.setId(VENDOR_ID);
        savedVendor.setName(VENDOR_NAME);


        VendorDTO newVendorDetails = new VendorDTO();
        newVendorDetails.setName(updated_name);
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(savedVendor));
        when(vendorRepository.save(any(Vendor.class))).thenAnswer(invocationOnMock -> invocationOnMock.<Vendor>getArgument(0));

        //when
        VendorDTO replacedVendor = vendorService.replaceVendor(VENDOR_ID, newVendorDetails);
        //then
        assertNotNull("Vendor is null", replacedVendor);
        assertEquals("Vendor Name not matching", updated_name, replacedVendor.getName());
    }

    @Test(expected = NotFoundException.class)
    public void replaceVendor_invalidId() {
        //Given
        final String updated_name = "Updated  Name";

        Vendor savedVendor = new Vendor();
        savedVendor.setId(VENDOR_ID);
        savedVendor.setName(VENDOR_NAME);


        VendorDTO newVendorDetails = new VendorDTO();
        newVendorDetails.setName(updated_name);
        when(vendorRepository.findById(anyLong())).thenThrow(new NotFoundException("No Vendor found for the given id"));
        when(vendorRepository.save(any(Vendor.class))).thenAnswer(invocationOnMock -> invocationOnMock.<Vendor>getArgument(0));

        //when
        VendorDTO replacedVendor = vendorService.replaceVendor(VENDOR_ID, newVendorDetails);
        //then
        assertNotNull("Vendor is null", replacedVendor);
        assertEquals("Vendor Name not matching", updated_name, replacedVendor.getName());
    }

    @Test
    public void updateVendor() {
        final String updated_name = "Updated Name";
        //Given
        Vendor savedVendor = new Vendor();
        savedVendor.setId(VENDOR_ID);
        savedVendor.setName(VENDOR_NAME);

        VendorDTO newVendorDetails = new VendorDTO();
        newVendorDetails.setName(updated_name);

        when(vendorRepository.findById(VENDOR_ID)).thenReturn(Optional.of(savedVendor));
        when(vendorRepository.save(any(Vendor.class))).thenAnswer(invocationOnMock -> invocationOnMock.<Vendor>getArgument(0));

        //when
        VendorDTO updatedVendor = vendorService.updateVendor(VENDOR_ID, newVendorDetails);
        //then
        assertNotNull("Vendor is null", updatedVendor);
        assertEquals("Name not matching", updated_name, updatedVendor.getName());
    }

    @Test(expected = NotFoundException.class)
    public void updateVendor_Invalid() {
        final String updated_name = "Updated Name";
        //Given
        Vendor savedVendor = new Vendor();
        savedVendor.setId(VENDOR_ID);
        savedVendor.setName(VENDOR_NAME);

        VendorDTO newVendorDetails = new VendorDTO();
        newVendorDetails.setName(updated_name);

        when(vendorRepository.findById(VENDOR_ID)).thenThrow(new NotFoundException("No Vendor found for the given id"));
        when(vendorRepository.save(any(Vendor.class))).thenAnswer(invocationOnMock -> invocationOnMock.<Vendor>getArgument(0));

        //when
        VendorDTO updatedVendor = vendorService.updateVendor(VENDOR_ID, newVendorDetails);
        //then
        assertNotNull("Vendor is null", updatedVendor);
        assertEquals("Name not matching", updated_name, updatedVendor.getName());
    }
}