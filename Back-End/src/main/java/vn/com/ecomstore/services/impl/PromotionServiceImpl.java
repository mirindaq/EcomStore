package vn.com.ecomstore.services.impl;

import vn.com.ecomstore.dtos.request.promotion.PromotionAddRequest;
import vn.com.ecomstore.dtos.request.promotion.PromotionUpdateRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.promotion.PromotionResponse;
import vn.com.ecomstore.entities.Promotion;
import vn.com.ecomstore.entities.PromotionTarget;
import vn.com.ecomstore.exceptions.custom.ResourceNotFoundException;
import vn.com.ecomstore.mappers.PromotionMapper;
import vn.com.ecomstore.repositories.PromotionRepository;
import vn.com.ecomstore.repositories.PromotionTargetRepository;
import vn.com.ecomstore.services.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionTargetRepository promotionTargetRepository;
    private final PromotionMapper promotionMapper;

    @Override
    @Transactional
    public PromotionResponse createPromotion(PromotionAddRequest request) {
        Promotion promotion = promotionMapper.toPromotion(request);

        promotionRepository.save(promotion);

        if (request.getPromotionTargets() != null) {
            List<PromotionTarget> promotionTargets = promotionMapper.toPromotionTargets(request.getPromotionTargets(), promotion);

            promotion.setPromotionTargets(promotionTargets);
            promotionTargetRepository.saveAll(promotionTargets);
        }

        return promotionMapper.toResponse(promotion);
    }


    @Override
    public PromotionResponse getPromotionById(Long id) {
        Promotion promotion = findById(id);
        return promotionMapper.toResponse(promotion);
    }

    @Override
    public ResponseWithPagination<List<PromotionResponse>> getAllPromotions(int page, int limit, String name,
                                                                            String type, Boolean active,
                                                                            LocalDate startDate, LocalDate endDate) {
        page = page > 0 ? page - 1 : page;
        Pageable pageable = PageRequest.of(page, limit);

        Page<Promotion> promotionPage = promotionRepository.searchPromotions(
                name, type, active, startDate, endDate, pageable
        );

        return ResponseWithPagination.fromPage(promotionPage, promotionMapper::toResponse);
    }

    @Override
    @Transactional
    public PromotionResponse updatePromotion(Long id, PromotionUpdateRequest request) {
        Promotion promotion = findById(id);

//        promotion.setName(request.getName());
//        promotion.setType(request.getType());
//        promotion.setDiscountType(request.getDiscountType());
//        promotion.setDiscountValue(request.getDiscountValue());
//        promotion.setPriority(request.getPriority());
//        promotion.setDescription(request.getDescription());
//        promotion.setStartDate(request.getStartDate());
//        promotion.setEndDate(request.getEndDate());

        promotionRepository.save(promotion);

        // Xoá targets cũ và thêm targets mới
        promotionTargetRepository.deleteByPromotion(promotion);
        if (request.getTargets() != null) {
            promotionTargetRepository.saveAll(promotionMapper.toPromotionTargets(request.getTargets(), promotion));
        }

        return promotionMapper.toResponse(promotion);
    }

    @Override
    @Transactional
    public void deletePromotion(Long id) {
        Promotion promotion = findById(id);
        promotionTargetRepository.deleteByPromotion(promotion);
        promotionRepository.delete(promotion);
    }

    @Override
    @Transactional
    public void changeStatusPromotion(Long id) {
        Promotion promotion = findById(id);
        promotion.setActive(!promotion.getActive());
        promotionRepository.save(promotion);
    }

    private Promotion findById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with id = " + id));
    }
}
