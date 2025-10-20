package vn.com.ecomstore.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import vn.com.ecomstore.dtos.request.product.ProductAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.product.ProductResponse;
import vn.com.ecomstore.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Controller for managing products")
public class ProductController {
    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<ResponseSuccess<?>> createProduct(@Valid @RequestBody ProductAddRequest productAddRequest) {
        productService.createProduct(productAddRequest);
        return ResponseEntity.ok(new ResponseSuccess<>(
                CREATED,
                "Create product success",
                null
        ));
    }

    @GetMapping("")
    public ResponseEntity<ResponseSuccess<ResponseWithPagination<List<ProductResponse>>>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "7") int size
    ) {
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Get all products success",
                productService.getAllProducts(page, size)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseSuccess<ProductResponse>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Get product detail success",
                productService.getProductById(id)
        ));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ResponseSuccess<ProductResponse>> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Get product detail by slug success",
                productService.getProductBySlug(slug)
        ));
    }


}
