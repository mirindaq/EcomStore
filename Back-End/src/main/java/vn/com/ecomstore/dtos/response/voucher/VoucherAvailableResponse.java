package vn.com.ecomstore.dtos.response.voucher;

import vn.com.ecomstore.enums.VoucherType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VoucherAvailableResponse {

    private Long id;

    private String code;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double minOrderAmount;

    private Double maxDiscountAmount;

    private Double discount;

    private VoucherType voucherType;

}
