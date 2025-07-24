package vn.com.ecomstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.ecomstore.entities.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}