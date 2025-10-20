package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.ProductVariantValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantValueRepository extends JpaRepository<ProductVariantValue, Long> {
}