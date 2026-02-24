package iuh.fit.ecommerce.mappers;

import iuh.fit.ecommerce.dtos.request.product.ProductAddRequest;
import iuh.fit.ecommerce.dtos.response.product.ProductResponse;
import iuh.fit.ecommerce.entities.Product;
import iuh.fit.ecommerce.entities.ProductVariant;
import iuh.fit.ecommerce.entities.Promotion;
import iuh.fit.ecommerce.services.PromotionService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {
                ProductAttributeValueMapper.class,
                ProductVariantMapper.class,
                VariantValueMapper.class,
                AttributeMapper.class,
                ProductImageMapper.class,
                ProductVariantValueMapper.class
        }
)
public interface ProductMapper {

    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    void requestToEntity(ProductAddRequest productAddRequest, @MappingTarget Product product);

    @Mapping(source = "productVariants", target = "variants")
    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "category.id", target = "categoryId")
    ProductResponse toResponse(Product product);
}
