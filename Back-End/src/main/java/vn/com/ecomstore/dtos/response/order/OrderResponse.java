package vn.com.ecomstore.dtos.response.order;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;

    private String orderCode;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private String note;

    private Boolean isPickup;

    private String paymentMethod;

    private String status;

    private Long totalAmount;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long productVariantId;
        private String productName;
        private String productImage;
        private String sku;
        private Integer quantity;
        private Long unitPrice;
        private Long subtotal; // unitPrice * quantity
    }
}
