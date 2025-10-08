package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.product.ProductAttributeResponse;
import vn.com.ecomstore.entities.ProductAttributeValue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AttributeMapper.class)
public interface ProductAttributeValueMapper {
    ProductAttributeResponse toResponse(ProductAttributeValue productAttributeValue);
}
