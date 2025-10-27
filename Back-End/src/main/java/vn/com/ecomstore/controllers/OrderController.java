package vn.com.ecomstore.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.ecomstore.dtos.request.customer.CustomerAddRequest;
import vn.com.ecomstore.dtos.request.order.OrderCreateRequest;
import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.customer.CustomerResponse;
import vn.com.ecomstore.dtos.response.order.OrderResponse;
import vn.com.ecomstore.services.OrderService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "")
    public ResponseEntity<ResponseSuccess<OrderResponse>> createUser(@Valid @RequestBody OrderCreateRequest orderCreateRequest){
        return ResponseEntity.ok(new ResponseSuccess<>(
                CREATED,
                "Create Customer success",
                orderService.createOrder(orderCreateRequest)
        ));
    }

}
