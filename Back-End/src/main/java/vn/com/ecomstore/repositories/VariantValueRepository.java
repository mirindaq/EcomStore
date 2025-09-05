package vn.com.ecomstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.ecomstore.entities.VariantValue;

import java.util.Collection;
import java.util.List;

public interface VariantValueRepository extends JpaRepository<VariantValue, Long> {
    boolean existsByValueAndVariantId(String value, Long variantId);

    List<VariantValue> findByVariant_Id(Long variantId);

    List<VariantValue> findByVariantId(Long variantId);
}