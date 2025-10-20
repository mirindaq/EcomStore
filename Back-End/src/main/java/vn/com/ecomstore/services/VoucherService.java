package vn.com.ecomstore.services;


import vn.com.ecomstore.dtos.request.voucher.VoucherAddRequest;
import vn.com.ecomstore.dtos.request.voucher.VoucherUpdateRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.voucher.VoucherAvailableResponse;
import vn.com.ecomstore.dtos.response.voucher.VoucherResponse;
import vn.com.ecomstore.entities.Voucher;

import java.time.LocalDate;
import java.util.List;

public interface VoucherService {
    VoucherResponse createVoucher( VoucherAddRequest request);

    VoucherResponse getVoucherById(Long id);

    ResponseWithPagination<List<VoucherResponse>> getAllVouchers(int page, int limit, String name, String type, Boolean active, LocalDate startDate, LocalDate endDate);

    VoucherResponse updateVoucher(Long id,  VoucherUpdateRequest request);

    void changeStatusVoucher(Long id);

    void sendVoucherToCustomers(Long id);

    Voucher getVoucherEntityById(Long id);

    List<VoucherAvailableResponse> getAvailableVouchersForCustomer();
}
