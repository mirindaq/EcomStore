package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.variant.VariantValueResponse;
import vn.com.ecomstore.entities.VariantValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VariantValueMapper {
    @Mapping(source = "variant.id", target = "variantId")
    @Mapping(source = "variant.name", target = "variantName")
    VariantValueResponse toResponse(VariantValue value);
}