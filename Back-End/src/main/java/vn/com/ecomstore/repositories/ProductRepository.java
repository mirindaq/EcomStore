package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    Optional<Product> findBySlug(String slug);

    Product getProductBySlug(String slug);
}