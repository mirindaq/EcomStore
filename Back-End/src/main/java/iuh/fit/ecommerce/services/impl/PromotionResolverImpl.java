package iuh.fit.ecommerce.services.impl;

import iuh.fit.ecommerce.dtos.request.search.SearchProductContext;
import iuh.fit.ecommerce.dtos.response.product.DisplayPriceResult;
import iuh.fit.ecommerce.entities.Promotion;
import iuh.fit.ecommerce.entities.PromotionTarget;
import iuh.fit.ecommerce.enums.PromotionType;
import iuh.fit.ecommerce.repositories.PromotionRepository;
import iuh.fit.ecommerce.services.PromotionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PromotionResolverImpl implements PromotionResolver {

    private final PromotionRepository promotionRepository;

    private Map<Long, Promotion> byVariantId;
    private Map<Long, Promotion> byProductId;
    private Map<Long, Promotion> byBrandId;
    private Map<Long, Promotion> byCategoryId;
    private Promotion bestGlobal;

    @Override
    public Map<Long, DisplayPriceResult> resolveDisplayPrices(List<SearchProductContext> contexts) {
        if (CollectionUtils.isEmpty(contexts)) return Map.of();

        Set<Long> variantIds = new HashSet<>();
        Set<Long> productIds = new HashSet<>();
        Set<Long> categoryIds = new HashSet<>();
        Set<Long> brandIds = new HashSet<>();
        for (SearchProductContext c : contexts) {
            if (c.getProductId() != null) productIds.add(c.getProductId());
            if (c.getVariantId() != null) variantIds.add(c.getVariantId());
            if (c.getCategoryId() != null) categoryIds.add(c.getCategoryId());
            if (c.getBrandId() != null) brandIds.add(c.getBrandId());
        }
        if (variantIds.isEmpty()) variantIds.add(-1L);
        if (categoryIds.isEmpty()) categoryIds.add(-1L);
        if (brandIds.isEmpty()) brandIds.add(-1L);

        List<Promotion> promotions = promotionRepository.findValidPromotionsWithTargetsForDisplayPrice(
                new ArrayList<>(variantIds), new ArrayList<>(productIds),
                new ArrayList<>(categoryIds), new ArrayList<>(brandIds));
        buildLookupMaps(promotions);

        Map<Long, DisplayPriceResult> result = new HashMap<>();
        for (SearchProductContext c : contexts) {
            Promotion best = resolveBestPromotion(c.getVariantId(), c.getProductId(), c.getBrandId(), c.getCategoryId());
            double orig = c.getOriginalPrice() != null ? c.getOriginalPrice() : 0.0;
            double discountPercent = (best != null && best.getDiscount() != null) ? best.getDiscount() : 0.0;
            double displayPrice = orig * (1 - discountPercent / 100.0);
            result.put(c.getProductId(), DisplayPriceResult.builder()
                    .productId(c.getProductId())
                    .originalPrice(orig)
                    .displayPrice(displayPrice)
                    .discountPercent(discountPercent)
                    .build());
        }
        return result;
    }

    @Override
    public Promotion resolveBestPromotion(Long variantId, Long productId, Long brandId, Long categoryId) {
        Promotion best = null;
        if (byVariantId != null && variantId != null) best = better(best, byVariantId.get(variantId));
        if (byProductId != null && productId != null) best = better(best, byProductId.get(productId));
        if (byBrandId != null && brandId != null) best = better(best, byBrandId.get(brandId));
        if (byCategoryId != null && categoryId != null) best = better(best, byCategoryId.get(categoryId));
        best = better(best, bestGlobal);
        return best;
    }

    private static Promotion better(Promotion current, Promotion candidate) {
        if (candidate == null) return current;
        if (current == null) return candidate;
        int pCur = current.getPriority() != null ? current.getPriority() : Integer.MAX_VALUE;
        int pCand = candidate.getPriority() != null ? candidate.getPriority() : Integer.MAX_VALUE;
        if (pCand != pCur) return pCand < pCur ? candidate : current;
        double dCur = current.getDiscount() != null ? current.getDiscount() : 0.0;
        double dCand = candidate.getDiscount() != null ? candidate.getDiscount() : 0.0;
        return dCand >= dCur ? candidate : current;
    }

    private void buildLookupMaps(List<Promotion> promotions) {
        byVariantId = new HashMap<>();
        byProductId = new HashMap<>();
        byBrandId = new HashMap<>();
        byCategoryId = new HashMap<>();
        bestGlobal = null;
        for (Promotion p : promotions) {
            if (p.getPromotionType() == PromotionType.ALL) {
                bestGlobal = better(bestGlobal, p);
                continue;
            }
            if (p.getPromotionTargets() == null) continue;
            for (PromotionTarget pt : p.getPromotionTargets()) {
                if (pt.getProductVariant() != null && pt.getProductVariant().getId() != null)
                    byVariantId.merge(pt.getProductVariant().getId(), p, PromotionResolverImpl::better);
                if (pt.getProduct() != null && pt.getProduct().getId() != null)
                    byProductId.merge(pt.getProduct().getId(), p, PromotionResolverImpl::better);
                if (pt.getBrand() != null && pt.getBrand().getId() != null)
                    byBrandId.merge(pt.getBrand().getId(), p, PromotionResolverImpl::better);
                if (pt.getCategory() != null && pt.getCategory().getId() != null)
                    byCategoryId.merge(pt.getCategory().getId(), p, PromotionResolverImpl::better);
            }
        }
    }
}
