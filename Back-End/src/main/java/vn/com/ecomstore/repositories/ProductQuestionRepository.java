package vn.com.ecomstore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.ecomstore.entities.Customer;
import vn.com.ecomstore.entities.Product;
import vn.com.ecomstore.entities.ProductQuestion;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ProductQuestionRepository extends JpaRepository<ProductQuestion, Long> {

    Page<ProductQuestion> findByProduct(Product product, Pageable pageable);
}
