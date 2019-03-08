package org.venkat.freshfruits.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.venkat.freshfruits.dto.VendorDTO;
import org.venkat.freshfruits.dto.VendorsListDataDTO;
import org.venkat.freshfruits.services.VendorService;

@RestController
@RequiredArgsConstructor
@Api(value = "Vendors", description = "REST API for Vendors", tags = {"Vendors"})
public class VendorController {

    private final VendorService vendorService;

    @GetMapping("/shop/vendors")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Lists all the vendors")
    public VendorsListDataDTO getAllVendors() {
        return vendorService.findAllVendors();
    }

    @GetMapping("/shop/vendors/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a vendor by id")
    public VendorDTO getVendorById(@PathVariable Long id) {
        return vendorService.findById(id);
    }

    @PostMapping("/shop/vendors")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a vendor")
    public VendorDTO createVendor(@RequestBody VendorDTO vendorDTO) {
        return vendorService.save(vendorDTO);
    }

    @PatchMapping("/shop/vendors/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update a vendor")
    public VendorDTO updateVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO) {
        return vendorService.updateVendor(id, vendorDTO);
    }

    @PutMapping("/shop/vendors/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Replace a vendor")
    public VendorDTO replaceVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO) {
        return vendorService.replaceVendor(id, vendorDTO);
    }

    @GetMapping("/shop/vendors/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete a vendor")
    public void deleteVendor(@PathVariable final Long id) {
        vendorService.deleteById(id);
    }
}
