package org.venkat.freshfruits.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.venkat.freshfruits.dto.VendorDTO;
import org.venkat.freshfruits.dto.VendorsListDataDTO;
import org.venkat.freshfruits.entity.Vendor;
import org.venkat.freshfruits.exceptions.NotFoundException;
import org.venkat.freshfruits.mappers.VendorMapper;
import org.venkat.freshfruits.repositories.VendorRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class VendorService {
    private static final String VENDORS_BASE_URL = "/shop/vendors/";

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public VendorsListDataDTO findAllVendors() {

        List<Vendor> vendors = StreamSupport.stream(vendorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return new VendorsListDataDTO(
                vendors.stream().map(vendor -> {
                    VendorDTO vendorDTO = vendorMapper.entityToDto(vendor);
                    vendorDTO.setVendorUrl(VENDORS_BASE_URL + vendor.getId());
                    return vendorDTO;
                }).collect(Collectors.toList()));
    }

    public VendorDTO save(VendorDTO vendorDTO) {
        Vendor savedVendor = vendorRepository.save(vendorMapper.dtoToEntity(vendorDTO));
        vendorDTO = vendorMapper.entityToDto(savedVendor);
        vendorDTO.setVendorUrl(VENDORS_BASE_URL + savedVendor.getId());
        return vendorDTO;
    }

    public void deleteById(Long id) {
        Vendor vendor = vendorRepository.findById(id).orElse(null);
        if (vendor == null) {
            throw new NotFoundException("No Vendor Found for given id " + id);
        }
        vendorRepository.deleteById(id);
    }

    public VendorDTO findById(Long id) {

        Vendor vendor = vendorRepository.findById(id).orElse(null);
        if (vendor == null) {
            throw new NotFoundException("No Vendor found for the Id: " + id);
        }
        return vendorMapper.entityToDto(vendor);
    }

    public VendorDTO replaceVendor(Long id, VendorDTO vendorDTO) {
        Vendor vendor = vendorRepository.findById(id).orElse(null);
        if (vendor == null) {
            throw new NotFoundException("No Vendor found for the Id: " + id);
        }
        vendor = vendorMapper.dtoToEntity(vendorDTO);
        vendor.setId(id);
        Vendor savedVendor = vendorRepository.save(vendor);
        return vendorMapper.entityToDto(savedVendor);
    }

    public VendorDTO updateVendor(Long id, VendorDTO vendorDTO) {

        Vendor vendor = vendorRepository.findById(id).orElse(null);
        if (vendor == null) {
            throw new NotFoundException("No Vendor found for the given id " + id);
        }

        if (!StringUtils.isEmpty(vendor.getName())) {
            vendor.setName(vendorDTO.getName());
        }
        return vendorMapper.entityToDto(vendorRepository.save(vendor));
    }
}
