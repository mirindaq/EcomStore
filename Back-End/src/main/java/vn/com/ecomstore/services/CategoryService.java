package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.category.CategoryAddRequest;
import vn.com.ecomstore.dtos.response.category.CategoryResponse;

public interface CategoryService {

    CategoryResponse createCategory(CategoryAddRequest request);
}
