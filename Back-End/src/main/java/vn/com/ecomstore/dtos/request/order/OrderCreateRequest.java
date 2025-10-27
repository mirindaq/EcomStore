package vn.com.ecomstore.dtos.request.order;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    @NotBlank
    private String receiverName;

    private String receiverAddress;

    @NotBlank
    private String receiverPhone;

    private String note;

    private Boolean isPickup = false;

    private String voucherCode;

    private List<OrderItemRequest> items;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {

        private Long productVariantId;

        private Long quantity;
    }
}
