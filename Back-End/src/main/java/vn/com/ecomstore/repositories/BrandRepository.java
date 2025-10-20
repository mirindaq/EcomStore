package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

    Page<Brand> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
