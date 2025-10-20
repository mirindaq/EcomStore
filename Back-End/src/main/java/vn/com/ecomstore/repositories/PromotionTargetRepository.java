package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.Promotion;
import vn.com.ecomstore.entities.PromotionTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionTargetRepository extends JpaRepository<PromotionTarget, Long> {
    void deleteByPromotion(Promotion promotion);
}
