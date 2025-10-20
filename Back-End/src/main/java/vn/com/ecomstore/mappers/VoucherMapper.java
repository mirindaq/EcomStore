package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.request.voucher.VoucherAddRequest;
import vn.com.ecomstore.dtos.response.voucher.VoucherAvailableResponse;
import vn.com.ecomstore.dtos.response.voucher.VoucherCustomerResponse;
import vn.com.ecomstore.dtos.response.voucher.VoucherResponse;
import vn.com.ecomstore.entities.Voucher;
import vn.com.ecomstore.entities.VoucherCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RankingMapper.class})
public interface VoucherMapper {

    @Mapping(target = "code", ignore = true)
    Voucher toVoucher(VoucherAddRequest voucherAddRequest);

    @Mapping(target = "voucherCustomers", source = "voucherCustomers")
    @Mapping(target = "ranking", source = "ranking")
    VoucherResponse toResponse(Voucher voucher);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "email", source = "customer.email")
    VoucherCustomerResponse toVoucherCustomerResponse(VoucherCustomer voucherCustomer);

    VoucherAvailableResponse toVoucherAvailableResponse(Voucher voucher);

    List<VoucherCustomerResponse> toVoucherCustomerResponses(List<VoucherCustomer> voucherCustomers);

}
