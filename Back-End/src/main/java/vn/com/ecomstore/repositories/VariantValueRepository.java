package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.VariantValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariantValueRepository extends JpaRepository<VariantValue, Long> {
    boolean existsByValueAndVariantId(String value, Long variantId);

    List<VariantValue> findByVariant_Id(Long variantId);

    List<VariantValue> findByVariantId(Long variantId);
}