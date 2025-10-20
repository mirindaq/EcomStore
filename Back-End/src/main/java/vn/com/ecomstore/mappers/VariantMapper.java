package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.variant.VariantResponse;
import vn.com.ecomstore.entities.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VariantValueMapper.class, CategoryMapper.class})
public interface VariantMapper {

    @Mapping(source = "variantValues", target = "variantValues")
    @Mapping(
            source = "category",
            target = "category",
            qualifiedByName = "toResponseWithoutAttributes"
    )
    VariantResponse toResponse(Variant variant);
}