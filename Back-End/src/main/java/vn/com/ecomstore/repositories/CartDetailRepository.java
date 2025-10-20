package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
}
