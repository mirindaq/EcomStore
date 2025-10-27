package vn.com.ecomstore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.ecomstore.dtos.request.order.OrderCreateRequest;
import vn.com.ecomstore.dtos.response.order.OrderResponse;
import vn.com.ecomstore.entities.*;
import vn.com.ecomstore.enums.OrderStatus;
import vn.com.ecomstore.enums.VoucherType;
import vn.com.ecomstore.exceptions.custom.ResourceNotFoundException;
import vn.com.ecomstore.mappers.OrderMapper;
import vn.com.ecomstore.repositories.OrderRepository;
import vn.com.ecomstore.repositories.ProductVariantRepository;
import vn.com.ecomstore.repositories.VoucherCustomerRepository;
import vn.com.ecomstore.repositories.VoucherRepository;
import vn.com.ecomstore.services.OrderService;
import vn.com.ecomstore.services.VoucherService;
import vn.com.ecomstore.utils.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final SecurityUtil securityUtil;
    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherCustomerRepository voucherCustomerRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {

        Customer currentUser = securityUtil.getCurrentCustomer();

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        // Khởi tạo order
        Order order = Order.builder()
                .receiverAddress(request.getIsPickup() ? null : request.getReceiverAddress())
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .status(OrderStatus.PENDING)
                .note(request.getNote())
                .isPickup(request.getIsPickup())
                .user(currentUser)
                .build();

        // Tạo chi tiết order và tính tổng tiền
        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderCreateRequest.OrderItemRequest item : request.getItems()) {
            ProductVariant productVariant = productVariantRepository.findById(item.getProductVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductVariant not found: " + item.getProductVariantId()));

            double subtotal = productVariant.getPrice() * item.getQuantity();
            totalAmount += subtotal;

            OrderDetail orderDetail = OrderDetail.builder()
                    .price(productVariant.getPrice())
                    .quantity(item.getQuantity().longValue())
                    .discount(0.0)
                    .finalPrice(subtotal)
                    .productVariant(productVariant)
                    .order(order)
                    .build();

            orderDetails.add(orderDetail);
        }

        order.setTotalPrice(totalAmount);

        double totalDiscount = 0.0;
        if (request.getVoucherCode() != null && !request.getVoucherCode().isBlank()) {
            Voucher voucher = applyVoucher(request.getVoucherCode(), currentUser, totalAmount);
            totalDiscount = voucher.getDiscount();
            order.setTotalDiscount(totalDiscount);
        } else {
            order.setTotalDiscount(0.0);
        }

        order.setFinalTotalPrice(totalAmount - totalDiscount);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }

    private Voucher applyVoucher(String code, Customer customer, double totalAmount) {
        if (code == null || code.isBlank()) return null;

        LocalDate now = LocalDate.now();

        Optional<Voucher> customerVoucherOpt = voucherRepository
                .findCustomerVoucherByCode(code, customer.getId());

        if (customerVoucherOpt.isPresent()) {
            Voucher voucher = customerVoucherOpt.get();

            if (!voucher.getActive() || now.isBefore(voucher.getStartDate()) || now.isAfter(voucher.getEndDate())) {
                throw new IllegalArgumentException("Voucher của bạn không còn hiệu lực");
            }

            return voucher;
        }

        Voucher globalVoucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher không tồn tại"));

        if (!globalVoucher.getActive() || now.isBefore(globalVoucher.getStartDate()) || now.isAfter(globalVoucher.getEndDate())) {
            throw new IllegalArgumentException("Voucher không còn hiệu lực");
        }

        return globalVoucher;
    }

}


