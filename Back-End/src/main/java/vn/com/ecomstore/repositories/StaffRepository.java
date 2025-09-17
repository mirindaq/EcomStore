package vn.com.ecomstore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.ecomstore.entities.Staff;

import java.time.LocalDate;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT s FROM Staff s " +
            "WHERE (:name IS NULL OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:email IS NULL OR LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:phone IS NULL OR s.phone LIKE CONCAT('%', :phone, '%')) " +
            "AND (:status IS NULL OR s.active = :status) " +
            "AND (:startDate IS NULL OR s.joinDate >= :startDate) " +
            "AND (:endDate IS NULL OR s.joinDate <= :endDate)")
    Page<Staff> findAllWithFilters(
            @Param("name") String name,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("status") Boolean status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

}
