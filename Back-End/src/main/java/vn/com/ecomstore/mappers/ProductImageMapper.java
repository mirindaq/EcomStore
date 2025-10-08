package vn.com.ecomstore.mappers;

import vn.com.ecomstore.entities.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    default String toResponse(ProductImage productImage) {
        return productImage != null ? productImage.getUrl() : null;
    }
}