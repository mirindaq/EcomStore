package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.category.CategoryAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.category.CategoryResponse;
import vn.com.ecomstore.entities.Category;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryAddRequest request);

    ResponseWithPagination<List<CategoryResponse>> getCategories( int page, int size, String categoryName);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id, @Valid CategoryAddRequest request);

    void changeStatusCategory(Long id);
    Category getCategoryEntityById(Long id);
}
