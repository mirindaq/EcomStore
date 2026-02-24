package iuh.fit.ecommerce.services;

import iuh.fit.ecommerce.dtos.response.base.PageResponse;
import iuh.fit.ecommerce.dtos.response.product.ProductSearchResponse;
import iuh.fit.ecommerce.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductSearchService {

    Optional<Product> loadProductForIndexing(Long productId);

    PageResponse<ProductSearchResponse> searchProducts(String query, int page, int size, String sortBy);

    List<String> getAutoCompleteSuggestions(String query, int limit);
    
    void indexProduct(Product product);
    
    void deleteProduct(Long productId);
    
    void reindexAllProducts();
}

