package vn.com.ecomstore.services;

import jakarta.validation.Valid;
import vn.com.ecomstore.dtos.request.order.OrderCreateRequest;
import vn.com.ecomstore.dtos.response.order.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(@Valid OrderCreateRequest orderCreateRequest);
}

