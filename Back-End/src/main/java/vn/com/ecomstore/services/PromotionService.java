package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.promotion.PromotionAddRequest;
import vn.com.ecomstore.dtos.request.promotion.PromotionUpdateRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.promotion.PromotionResponse;

import java.time.LocalDate;
import java.util.List;

public interface PromotionService {
    PromotionResponse createPromotion(PromotionAddRequest request);
    PromotionResponse getPromotionById(Long id);
    ResponseWithPagination<List<PromotionResponse>> getAllPromotions(
            int page, int limit,
            String name, String type,
            Boolean active,
            LocalDate startDate, LocalDate endDate
    );
    PromotionResponse updatePromotion(Long id, PromotionUpdateRequest request);
    void deletePromotion(Long id);
    void changeStatusPromotion(Long id);
}
