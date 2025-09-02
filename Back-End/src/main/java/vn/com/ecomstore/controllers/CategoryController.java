package vn.com.ecomstore.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.ecomstore.dtos.request.category.CategoryAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.category.CategoryResponse;
import vn.com.ecomstore.services.CategoryService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

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
