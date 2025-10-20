package vn.com.ecomstore.dtos.request.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartUpdateQuantityRequest {
    private Long productVariantId;
    private Integer quantity;
}