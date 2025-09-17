
package vn.com.ecomstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.ecomstore.entities.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    boolean existsByName(String roleName);

    List<Role> findAllByIdIn(List<Long> roleIds);
}