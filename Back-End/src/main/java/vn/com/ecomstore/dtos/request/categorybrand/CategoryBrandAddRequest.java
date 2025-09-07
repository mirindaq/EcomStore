package vn.com.ecomstore.dtos.request.categorybrand;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryBrandAddRequest {

    private List<Long> brands;
    @NotNull(message = "Category id is required")
    private Long categoryId;
}
