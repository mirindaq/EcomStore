package vn.com.ecomstore.repositories;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.ecomstore.dtos.response.categorybrand.CategoryWithBrandCountResponse;

import java.util.List;

public interface CategoryBrandRepository extends JpaRepository<vn.com.ecomstore.entities.CategoryBrand, Long> {

    @Modifying
    @Query("DELETE FROM CategoryBrand cb WHERE cb.category.id = :categoryId")
    void deleteByCategoryId(@Param("categoryId") Long categoryId);

    @Query("""
    SELECT c.id, c.name, COUNT(cb.id)
    FROM Category c
    LEFT JOIN CategoryBrand cb ON cb.category.id = c.id
    GROUP BY c.id, c.name
    """)
    Page<Object[]> findCategoriesWithBrandCount(Pageable pageable);

    @Query("SELECT cb.brand.id FROM CategoryBrand cb WHERE cb.category.id = :categoryId")
    List<Long> findBrandIdsByCategoryId(@Param("categoryId") Long categoryId);



}
