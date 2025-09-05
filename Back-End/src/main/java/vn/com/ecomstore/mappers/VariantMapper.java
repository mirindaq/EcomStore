package vn.com.ecomstore.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.ecomstore.dtos.response.variant.VariantResponse;
import vn.com.ecomstore.entities.Variant;

@Mapper(componentModel = "spring", uses = {VariantValueMapper.class})
public interface VariantMapper {

    @Mapping(source = "variantValues", target = "variantValues")
    VariantResponse toResponse(Variant variant);
}