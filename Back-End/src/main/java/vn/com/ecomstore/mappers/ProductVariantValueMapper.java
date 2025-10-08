package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.product.ProductVariantValueResponse;
import vn.com.ecomstore.entities.ProductVariantValue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = VariantValueMapper.class)
public interface ProductVariantValueMapper {
    ProductVariantValueResponse toResponse(ProductVariantValue entity);
}

