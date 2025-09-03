package vn.com.ecomstore.services;

import jakarta.validation.Valid;
import vn.com.ecomstore.dtos.request.category.CategoryAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryAddRequest request);

    ResponseWithPagination<List<CategoryResponse>> getCategories( int page, int size);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id, @Valid CategoryAddRequest request);

    void changeStatusCategory(Long id);
}
