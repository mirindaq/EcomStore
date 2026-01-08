package iuh.fit.ecommerce.services;

import iuh.fit.ecommerce.dtos.response.base.PageResponse;
import iuh.fit.ecommerce.dtos.response.product.ProductResponse;
import iuh.fit.ecommerce.entities.Product;

import java.util.List;

public interface ProductSearchService {
    PageResponse<ProductResponse> searchProducts(
            String query,
            int page,
            int size,
            String sortBy
    );
    

    List<String> getAutoCompleteSuggestions(String query, int limit);
    
    void indexProduct(Product product);
    
    void deleteProduct(Long productId);
    
    void reindexAllProducts();
}

