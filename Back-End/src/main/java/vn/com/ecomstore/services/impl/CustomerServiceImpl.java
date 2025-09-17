package vn.com.ecomstore.services.impl;

import  vn.com.ecomstore.dtos.request.customer.CustomerAddRequest;
import  vn.com.ecomstore.dtos.request.customer.CustomerProfileRequest;
import  vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import  vn.com.ecomstore.dtos.response.customer.CustomerResponse;
import  vn.com.ecomstore.entities.Cart;
import  vn.com.ecomstore.entities.Customer;
import  vn.com.ecomstore.entities.Role;
import  vn.com.ecomstore.entities.UserRole;
import  vn.com.ecomstore.exceptions.custom.ResourceNotFoundException;
import  vn.com.ecomstore.mappers.CustomerMapper;
import  vn.com.ecomstore.repositories.CartRepository;
import  vn.com.ecomstore.repositories.CustomerRepository;
import  vn.com.ecomstore.repositories.RoleRepository;
import  vn.com.ecomstore.repositories.UserRoleRepository;
import  vn.com.ecomstore.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final UserRoleRepository userRoleRepository;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerAddRequest request) {
        Role role = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Role CUSTOMER not exist"));

        Customer customer = customerMapper.toCustomer(request);
        userRepository.save(customer);
        UserRole userRole = UserRole.builder()
                .user(customer)
                .role(role)
                .build();
        userRoleRepository.save(userRole);
        Cart cart = Cart.builder()
                .user(customer)
                .build();
        cartRepository.save(cart);
        return customerMapper.toResponse(customer, role.getName());
    }

    @Override
    public CustomerResponse getCustomerById(long id) {
        Customer customer = findById(id);
        return customerMapper.toResponse(customer);
    }


    @Override
    public ResponseWithPagination<List<CustomerResponse>> getAllCustomers(int page, int limit, String name,
                                                                          String phone, String email,
                                                                          Boolean status, LocalDate startDate, LocalDate endDate ) {
        page = page > 0 ? page - 1 : page;
        Pageable pageable = PageRequest.of(page, limit);

        Page<Customer> customerPage = customerRepository.searchCustomers(name, phone, email, status, startDate, endDate, pageable);
        return ResponseWithPagination.fromPage(customerPage, customerMapper::toResponse);
    }




    @Override
    @Transactional
    public CustomerResponse updateCustomer(long id, CustomerProfileRequest customerProfileRequest) {
        Customer customer = findById(id);
        customer.setFullName(customerProfileRequest.getFullName());
        customer.setPhone(customerProfileRequest.getPhone());
        customer.setEmail(customerProfileRequest.getEmail());
        customer.setAddress(customerProfileRequest.getAddress());
        customer.setAvatar(customerProfileRequest.getAvatar());
        customer.setDateOfBirth(customerProfileRequest.getDateOfBirth());
        userRepository.save(customer);
        return customerMapper.toResponse(customer);
    }


    public Customer findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id = " + id));
    }

    @Override
    public void changeStatusCustomer(Long id) {
        Customer customer = findById(id);
        customerRepository.save(customer);
    }


}
