package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}