package vn.com.ecomstore.mappers;


import vn.com.ecomstore.dtos.response.promotion.PromotionTargetResponse;
import vn.com.ecomstore.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PromotionTargetMapper {

    @Mapping(source = "productVariant.id", target = "productVariantId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "brand.id", target = "brandId")
    PromotionTargetResponse toResponse(PromotionTarget request);
}