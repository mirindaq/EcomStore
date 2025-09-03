package vn.com.ecomstore.mappers;

import org.mapstruct.Mapper;
import vn.com.ecomstore.dtos.response.brand.BrandResponse;
import vn.com.ecomstore.entities.Brand;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandResponse toResponse(Brand brand);
}
