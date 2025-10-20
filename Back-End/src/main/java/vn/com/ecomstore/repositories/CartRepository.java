package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.Cart;
import vn.com.ecomstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    void deleteByUser(User user);

    Optional<Cart> findByUser_Id(Long userId);
}

