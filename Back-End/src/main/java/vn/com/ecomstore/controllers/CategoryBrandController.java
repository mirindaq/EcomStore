package vn.com.ecomstore.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.ecomstore.dtos.request.categorybrand.CategoryBrandAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.categorybrand.CategoryWithBrandCountResponse;
import vn.com.ecomstore.services.CategoryBrandService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/category-brands")
@RequiredArgsConstructor
public class CategoryBrandController {

    private final CategoryBrandService categoryBrandService;

    @GetMapping("")
    public ResponseEntity<ResponseSuccess<ResponseWithPagination<List<CategoryWithBrandCountResponse>>>> getCategoriesWithBrandCount(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "7") int size
    ) {
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Get categories with brand count success",
                categoryBrandService.getCategoriesWithBrandCount(page, size)
        ));
    }


    @GetMapping("/{categoryId}/brands")
    public ResponseEntity<ResponseSuccess<List<Long>>> getBrandIdsByCategoryId(
            @PathVariable Long categoryId
    ) {
        List<Long> brandIds = categoryBrandService.getBrandIdsByCategoryId(categoryId);
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Get brand IDs by category ID success",
                brandIds
        ));
    }

    @PostMapping("")
    public ResponseEntity<ResponseSuccess<Void>> assignBrandToCategory(
            @Valid @RequestBody CategoryBrandAddRequest request
    ) {
        categoryBrandService.assignBrandsToCategory(request);
        return ResponseEntity.ok(new ResponseSuccess<>(
                CREATED,
                "Assign brand to category success",
                null
        ));
    }

}
