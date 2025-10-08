package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.product.ProductAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.product.ProductResponse;

import java.util.List;

public interface ProductService {
    void createProduct(ProductAddRequest productAddRequest);
    ResponseWithPagination<List<ProductResponse>> getAllProducts(int page, int size);
    ProductResponse getProductById(Long id);
    ProductResponse getProductBySlug(String slug);
    ProductResponse updateProductById(Long id);
}
