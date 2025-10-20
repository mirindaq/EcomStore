package vn.com.ecomstore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.ecomstore.dtos.request.categorybrand.CategoryBrandAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.categorybrand.CategoryWithBrandCountResponse;
import vn.com.ecomstore.entities.Brand;
import vn.com.ecomstore.entities.Category;
import vn.com.ecomstore.entities.CategoryBrand;
import vn.com.ecomstore.exceptions.custom.ResourceNotFoundException;
import vn.com.ecomstore.repositories.BrandRepository;
import vn.com.ecomstore.repositories.CategoryBrandRepository;
import vn.com.ecomstore.repositories.CategoryRepository;
import vn.com.ecomstore.services.CategoryBrandService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryBrandServiceImpl implements CategoryBrandService {

    private final CategoryBrandRepository categoryBrandRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public void assignBrandsToCategory(CategoryBrandAddRequest request) {

        if (request.getBrands().isEmpty()) {
            Long categoryId = request.getCategoryId();
            categoryBrandRepository.deleteByCategoryId(categoryId);
            return;
        }
        categoryBrandRepository.deleteByCategoryId(request.getCategoryId());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        List<CategoryBrand> categoryBrands = request.getBrands().stream()
                .map(req -> {
                    Brand brand = brandRepository.findById(req)
                            .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + req));

                    CategoryBrand cb = new CategoryBrand();
                    cb.setCategory(category);
                    cb.setBrand(brand);
                    return cb;
                })
                .toList();

        categoryBrandRepository.saveAll(categoryBrands);
    }



    @Override
    public ResponseWithPagination<List<CategoryWithBrandCountResponse>> getCategoriesWithBrandCount(int page, int size) {
        page = Math.max(0, page - 1);
        PageRequest pageable = PageRequest.of(page, size);

        Page<Object[]> result = categoryBrandRepository.findCategoriesWithBrandCount(pageable);

        Page<CategoryWithBrandCountResponse> mapped = result.map(row ->
                CategoryWithBrandCountResponse.builder()
                        .categoryId((Long) row[0])
                        .categoryName((String) row[1])
                        .brandCount((Long) row[2])
                        .build()
        );

        return ResponseWithPagination.fromPage(mapped, r -> r);
    }

    @Override
    public List<Long> getBrandIdsByCategoryId(Long categoryId) {
        return categoryBrandRepository.findBrandIdsByCategoryId(categoryId);
    }


}