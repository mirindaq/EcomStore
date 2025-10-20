package vn.com.ecomstore.dtos.response.categorybrand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryWithBrandCountResponse {
    private Long categoryId;
    private String categoryName;
    private Long brandCount;
}