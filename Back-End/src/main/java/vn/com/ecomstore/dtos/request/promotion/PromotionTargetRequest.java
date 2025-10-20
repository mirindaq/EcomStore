package vn.com.ecomstore.dtos.request.promotion;

import jakarta.validation.constraints.Positive;
import vn.com.ecomstore.validation.ValidPromotionTarget;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidPromotionTarget
public class PromotionTargetRequest {

    @Positive(message = "productId must be greater than 0")
    private Long productId;

    @Positive(message = "productVariantId must be greater than 0")
    private Long productVariantId;

    @Positive(message = "categoryId must be greater than 0")
    private Long categoryId;

    @Positive(message = "brandId must be greater than 0")
    private Long brandId;
}
