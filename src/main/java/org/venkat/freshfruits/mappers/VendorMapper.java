package org.venkat.freshfruits.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.venkat.freshfruits.dto.VendorDTO;
import org.venkat.freshfruits.entity.Vendor;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    VendorMapper vendorMapper = Mappers.getMapper(VendorMapper.class);

    VendorDTO entityToDto(Vendor vendor);

    Vendor dtoToEntity(VendorDTO vendorDTO);
}
