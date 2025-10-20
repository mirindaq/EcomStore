package vn.com.ecomstore.dtos.response.voucher;

import vn.com.ecomstore.enums.DiscountType;
import vn.com.ecomstore.enums.VoucherType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class VoucherResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double discount;
    private Double minOrderAmount;
    private Double maxDiscountAmount;
    private Boolean active;
    private VoucherType voucherType;

    private List<VoucherCustomerResponse> voucherCustomers;
    private RankVoucherResponse ranking;
}
