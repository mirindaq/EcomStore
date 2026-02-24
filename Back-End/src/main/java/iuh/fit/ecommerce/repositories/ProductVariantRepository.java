package iuh.fit.ecommerce.repositories;

import iuh.fit.ecommerce.dtos.projection.MinVariantPriceProjection;
import iuh.fit.ecommerce.entities.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    List<ProductVariant> findByIdIn(List<Long> ids);

    @Query(value = """
        SELECT t.product_id AS productId, t.variant_id AS variantId, t.price AS price, t.brand_id AS brandId, t.category_id AS categoryId, t.sku AS sku, t.stock AS stock
        FROM (
            SELECT pv.product_id, pv.id AS variant_id, pv.price, pv.sku, pv.stock, p.brand_id, p.category_id,
                   ROW_NUMBER() OVER (PARTITION BY pv.product_id ORDER BY pv.price ASC, pv.id ASC) AS rn
            FROM product_variants pv
            INNER JOIN products p ON pv.product_id = p.id
            WHERE p.id IN (:productIds)
        ) t
        WHERE t.rn = 1
        """, nativeQuery = true)
    List<MinVariantPriceProjection> findMinPriceVariantByProductIds(@Param("productIds") List<Long> productIds);
}
