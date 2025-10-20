package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.brand.BrandResponse;
import vn.com.ecomstore.entities.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandResponse toResponse(Brand brand);
}
