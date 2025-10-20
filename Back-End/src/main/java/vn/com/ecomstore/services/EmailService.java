package vn.com.ecomstore.services;

import vn.com.ecomstore.entities.Voucher;

public interface EmailService {

    void sendVoucher(String to, Voucher voucher, String code);
}
