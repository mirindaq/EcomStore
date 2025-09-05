package vn.com.ecomstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.ecomstore.entities.Attribute;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute,Long> {

    List<Attribute> findByStatus(boolean status);

}
