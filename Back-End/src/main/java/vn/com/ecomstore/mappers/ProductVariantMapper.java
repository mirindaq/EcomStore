package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.product.ProductVariantResponse;
import vn.com.ecomstore.entities.ProductVariant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ProductVariantValueMapper.class })
public interface ProductVariantMapper {
    ProductVariantResponse toResponse(ProductVariant productVariant);
}
