package vn.com.ecomstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.ecomstore.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
}