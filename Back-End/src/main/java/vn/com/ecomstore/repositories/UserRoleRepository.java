package vn.com.ecomstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.ecomstore.entities.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
