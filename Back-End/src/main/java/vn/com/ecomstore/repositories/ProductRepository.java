package vn.com.ecomstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.ecomstore.entities.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    Optional<Product> findBySlug(String slug);
}