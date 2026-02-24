package iuh.fit.ecommerce.services;

import iuh.fit.ecommerce.dtos.request.search.SearchProductContext;
import iuh.fit.ecommerce.dtos.response.product.DisplayPriceResult;
import iuh.fit.ecommerce.entities.Promotion;

import java.util.List;
import java.util.Map;

public interface PromotionResolver {

    Map<Long, DisplayPriceResult> resolveDisplayPrices(List<SearchProductContext> contexts);

    Promotion resolveBestPromotion(Long variantId, Long productId, Long brandId, Long categoryId);
}
