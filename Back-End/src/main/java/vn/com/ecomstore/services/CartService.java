package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.cart.CartAddRequest;
import vn.com.ecomstore.dtos.request.cart.CartUpdateQuantityRequest;
import vn.com.ecomstore.dtos.response.cart.CartResponse;

public interface CartService {
    CartResponse getOrCreateCart();

    CartResponse addProduct(CartAddRequest request);

    CartResponse removeProduct(Long productVariantId);

    void clearCart(Long userId);

    CartResponse updateProductQuantity(CartUpdateQuantityRequest request);
}
