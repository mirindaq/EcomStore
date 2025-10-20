package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
}