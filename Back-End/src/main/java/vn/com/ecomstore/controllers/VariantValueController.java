package vn.com.ecomstore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.variant.VariantValueResponse;
import vn.com.ecomstore.services.VariantValueService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/variant-values")
@RequiredArgsConstructor
public class VariantValueController {

    private final VariantValueService variantValueService;

    @GetMapping("/variant/{id}")
    public ResponseEntity<ResponseSuccess<List<VariantValueResponse>>> getVariantValueByVariantId(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Get variant value detail success",
                variantValueService.getVariantValueByVariantId(id)
        ));
    }

}

