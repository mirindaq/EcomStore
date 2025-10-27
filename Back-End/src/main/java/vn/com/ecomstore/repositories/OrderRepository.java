package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

}