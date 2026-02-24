package iuh.fit.ecommerce.dtos.request.search;

import lombok.Builder;
import lombok.Getter;

/**
 * Minimal context for promotion resolution (batch display price).
 */
@Getter
@Builder
public class SearchProductContext {
    private final Long productId;
    private final Long brandId;
    private final Long categoryId;
    private final Long variantId;
    private final Double originalPrice;
}
