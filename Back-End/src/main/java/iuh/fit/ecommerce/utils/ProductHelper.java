package iuh.fit.ecommerce.utils;

import iuh.fit.ecommerce.entities.Product;
import iuh.fit.ecommerce.entities.ProductVariant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ProductHelper {

    public Double minPrice(Product product) {
        return product.getProductVariants().stream()
                .map(ProductVariant::getPrice)
                .filter(Objects::nonNull)
                .min(Double::compareTo)
                .orElse(null);
    }

    public Double maxPrice(Product product) {
        return product.getProductVariants().stream()
                .map(ProductVariant::getPrice)
                .filter(Objects::nonNull)
                .max(Double::compareTo)
                .orElse(null);
    }

    public Integer totalStock(Product product) {
        return product.getProductVariants().stream()
                .map(ProductVariant::getStock)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    public List<String> variantValues(Product product) {
        return product.getProductVariants().stream()
                .flatMap(v -> v.getProductVariantValues().stream())
                .map(pvv -> pvv.getVariantValue().getValue())
                .filter(Objects::nonNull)
                .toList();
    }

    public List<String> variantSkus(Product product) {
        return product.getProductVariants().stream()
                .map(ProductVariant::getSku)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<String> attributeNames(Product product) {
        return product.getAttributes().stream()
                .map(a -> a.getAttribute().getName())
                .filter(Objects::nonNull)
                .toList();
    }

    public List<String> attributeValues(Product product) {
        return product.getAttributes().stream()
                .map(a -> a.getValue())
                .filter(v -> v != null && !v.isBlank())
                .toList();
    }

    public List<String> filterValues(Product product) {
        return product.getProductFilterValues().stream()
                .map(f -> f.getFilterValue().getValue())
                .filter(Objects::nonNull)
                .toList();
    }

    public List<String> searchableText(Product product) {

        List<String> result = new ArrayList<>();

        add(result, product.getName());
        add(result, product.getDescription());

        if (product.getBrand() != null)
            add(result, product.getBrand().getName());

        if (product.getCategory() != null)
            add(result, product.getCategory().getName());

        result.addAll(variantValues(product));
        result.addAll(attributeValues(product));
        result.addAll(filterValues(product));
        result.addAll(variantSkus(product));

        return result;
    }

    private void add(List<String> list, String value) {
        if (value != null && !value.isBlank()) {
            list.add(value);
        }
    }
}