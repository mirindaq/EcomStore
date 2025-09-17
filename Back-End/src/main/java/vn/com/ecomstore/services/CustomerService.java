package vn.com.ecomstore.services;


import  vn.com.ecomstore.dtos.request.customer.CustomerAddRequest;
import  vn.com.ecomstore.dtos.request.customer.CustomerProfileRequest;
import  vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import  vn.com.ecomstore.dtos.response.customer.CustomerResponse;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerAddRequest customerAddRequest);

    CustomerResponse getCustomerById(long id);

    ResponseWithPagination<List<CustomerResponse>> getAllCustomers(int page, int limit, String name, String phone, String email, Boolean status, LocalDate startDate, LocalDate endDate);

    CustomerResponse updateCustomer(long id, CustomerProfileRequest request);

    void changeStatusCustomer(Long id);
}
