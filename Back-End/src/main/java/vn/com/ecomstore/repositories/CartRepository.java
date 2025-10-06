package vn.com.ecomstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.ecomstore.entities.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_Id(Long userId);
}
