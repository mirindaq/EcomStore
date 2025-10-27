package vn.com.ecomstore.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.com.ecomstore.dtos.response.order.OrderResponse;
import vn.com.ecomstore.entities.Order;
import vn.com.ecomstore.entities.OrderDetail;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

//    @Mapping(source = "order.id", target = "orderId")
//    @Mapping(source = "order.user.id", target = "userId")
//    @Mapping(source = "order.orderDetails", target = "items")
//    @Mapping(source = "order.totalPrice", target = "totalPrice")
//    @Mapping(source = "order.totalDiscount", target = "totalDiscount")
//    @Mapping(source = "order.finalTotalPrice", target = "finalTotalPrice")
    OrderResponse toResponse(Order order);

    @Mapping(source = "productVariant.id", target = "productVariantId")
    @Mapping(source = "productVariant.product.name", target = "productName")
    @Mapping(source = "productVariant.product.thumbnail", target = "productImage")
    @Mapping(source = "productVariant.sku", target = "sku")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "price", target = "unitPrice")
    @Mapping(source = "finalPrice", target = "subtotal")
    OrderResponse.OrderItemResponse toItemResponse(OrderDetail orderDetail);

    @Named("calculateTotalFinalPrice")
    default double calculateTotalFinalPrice(List<OrderDetail> orderDetails) {
        if (orderDetails == null) return 0;
        return orderDetails.stream()
                .mapToDouble(od -> od.getFinalPrice() != null ? od.getFinalPrice() : 0)
                .sum();
    }
}
