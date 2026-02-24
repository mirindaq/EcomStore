package iuh.fit.ecommerce.services;

import iuh.fit.ecommerce.dtos.response.base.PageResponse;
import iuh.fit.ecommerce.dtos.response.product.ProductResponse;
import iuh.fit.ecommerce.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductSearchService {

    /** Load product đầy đủ cho indexing trong 1 transaction (4 round-trips DB, 1 entity). */
    Optional<Product> loadProductForIndexing(Long productId);
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

