package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.categorybrand.CategoryBrandAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.categorybrand.CategoryWithBrandCountResponse;

import java.util.List;

public interface CategoryBrandService {
    void  assignBrandsToCategory(CategoryBrandAddRequest request);
    ResponseWithPagination<List<CategoryWithBrandCountResponse>> getCategoriesWithBrandCount(int page, int size);
    List<Long> getBrandIdsByCategoryId(Long categoryId);
}
