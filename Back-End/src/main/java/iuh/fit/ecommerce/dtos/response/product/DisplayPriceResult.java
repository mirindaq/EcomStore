package iuh.fit.ecommerce.dtos.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DisplayPriceResult {
    private Long productId;
    private Double originalPrice;
    private Double displayPrice;
    private Double discountPercent;
}
