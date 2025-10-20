package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    boolean existsByName(String name);

    Ranking findByName(String name);
}
