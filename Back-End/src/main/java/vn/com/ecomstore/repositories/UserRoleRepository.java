package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.Role;
import vn.com.ecomstore.entities.User;
import vn.com.ecomstore.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    boolean existsByRole(Role role);
    void deleteByUser(User user);

}
