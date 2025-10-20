package vn.com.ecomstore.controllers;

import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.variant.VariantValueResponse;
import vn.com.ecomstore.services.VariantValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

