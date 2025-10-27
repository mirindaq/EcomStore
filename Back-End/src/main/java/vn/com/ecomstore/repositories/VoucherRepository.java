package vn.com.ecomstore.repositories;

import vn.com.ecomstore.entities.Voucher;
import vn.com.ecomstore.enums.VoucherType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query("""
        SELECT v FROM Voucher v
        WHERE (:name IS NULL OR v.name LIKE %:name%)
          AND (:type IS NULL OR v.voucherType = :type)
          AND (:active IS NULL OR v.active = :active)
          AND (:startDate IS NULL OR v.startDate >= :startDate)
          AND (:endDate IS NULL OR v.endDate <= :endDate)
        """)
    Page<Voucher> searchVouchers(@Param("name") String name,
                                 @Param("type") VoucherType type,
                                 @Param("active") Boolean active,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate, Pageable pageable);

    List<Voucher> findAllByVoucherTypeAndEndDateGreaterThanEqualAndStartDateLessThanEqual(VoucherType voucherType
            , LocalDate endDateIsGreaterThan
            , LocalDate startDateIsLessThan);

    boolean existsByCode(String code);

    @Query("""
    SELECT vc.voucher
    FROM VoucherCustomer vc
    WHERE vc.customer.id = :customerId AND vc.code = :code
""")
    Optional<Voucher> findCustomerVoucherByCode(@Param("code") String code,
                                                @Param("customerId") Long customerId);

    Optional<Voucher> findByCode(String code); // voucher global



}