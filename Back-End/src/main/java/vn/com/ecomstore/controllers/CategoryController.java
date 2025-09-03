package vn.com.ecomstore.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.ecomstore.dtos.request.category.CategoryAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.category.CategoryResponse;
import vn.com.ecomstore.services.CategoryService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/admin")
    public ResponseEntity<ResponseSuccess<ResponseWithPagination<List<CategoryResponse>>>> getCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Get categories success",
                categoryService.getCategories( page, size)
        ));
    }


    @PostMapping("")
    public ResponseEntity<ResponseSuccess<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryAddRequest request) {

        return ResponseEntity.ok(new ResponseSuccess<>(
                CREATED,
                "Create category success",
                categoryService.createCategory(request)
        ));
    }
}
