package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute,Long> {

    List<Attribute> findByStatus(boolean status);

}
