package vn.com.ecomstore.dtos.response.promotion;

import vn.com.ecomstore.enums.PromotionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class PromotionResponse {
    private Long id;
    private String name;
    private PromotionType promotionType;
    private Double discount;
    private Boolean active;
    private Integer priority;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<PromotionTargetResponse> promotionTargets;
}
