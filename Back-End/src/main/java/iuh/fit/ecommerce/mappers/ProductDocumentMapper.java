package iuh.fit.ecommerce.mappers;

import iuh.fit.ecommerce.entities.Product;
import iuh.fit.ecommerce.entities.elasticsearch.ProductDocument;
import iuh.fit.ecommerce.utils.ProductHelper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = ProductHelper.class
)
public interface ProductDocumentMapper {

    @Mapping(target = "id", expression = "java(String.valueOf(product.getId()))")
    @Mapping(target = "productId", source = "id")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "brandName", source = "brand.name")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "categorySlug", source = "category.slug")
    @Mapping(target = "minPrice", expression = "java(helper.minPrice(product))")
    @Mapping(target = "maxPrice", expression = "java(helper.maxPrice(product))")
    @Mapping(target = "stock", expression = "java(helper.totalStock(product))")
    @Mapping(target = "variantValues",
            expression = "java(helper.variantValues(product))")
    @Mapping(target = "variantSkus",
            expression = "java(helper.variantSkus(product))")
    @Mapping(target = "attributeNames",
            expression = "java(helper.attributeNames(product))")
    @Mapping(target = "attributeValues",
            expression = "java(helper.attributeValues(product))")
    @Mapping(target = "filterValues",
            expression = "java(helper.filterValues(product))")
    @Mapping(target = "searchableText",
            expression = "java(helper.searchableText(product))")
    ProductDocument toDocument(Product product, @Context ProductHelper helper);
}