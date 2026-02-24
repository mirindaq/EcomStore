package iuh.fit.ecommerce.services;

import iuh.fit.ecommerce.dtos.request.product.ProductAddRequest;
import iuh.fit.ecommerce.dtos.request.product.ProductUpdateRequest;
import iuh.fit.ecommerce.dtos.response.base.PageResponse;
import iuh.fit.ecommerce.dtos.response.product.ProductResponse;
import iuh.fit.ecommerce.dtos.response.product.ProductSearchResponse;
import iuh.fit.ecommerce.entities.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    void createProduct(ProductAddRequest productAddRequest);
    PageResponse<ProductResponse> getAllProducts(int page, int size, String keyword, Long brandId, Long categoryId, Boolean status, Double minPrice, Double maxPrice);
    ProductResponse getProductById(Long id);
    ProductResponse getProductBySlug(String slug);
    ProductResponse updateProduct(Long id, ProductUpdateRequest productUpdateRequest);
    void changeStatusProduct(Long id);

    Product getProductEntityById(Long id);

    Product getProductEntityBySlug(String slug);

    PageResponse<ProductSearchResponse> searchProductForUser(String categorySlug, int page, int size, Map<String, String> filters);
}
