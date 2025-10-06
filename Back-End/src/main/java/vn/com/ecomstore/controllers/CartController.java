package vn.com.ecomstore.controllers;

import vn.com.ecomstore.dtos.request.cart.CartAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.cart.CartResponse;
import vn.com.ecomstore.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("")
    public ResponseEntity<ResponseSuccess<CartResponse>> getCart() {
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Get cart success",
                cartService.getOrCreateCart()
        ));
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseSuccess<CartResponse>> addProductToCart(
            @RequestBody CartAddRequest request
    ) {
        return ResponseEntity.ok(new ResponseSuccess<>(
                CREATED,
                "Add product to cart success",
                cartService.addProduct( request)
        ));
    }

    @DeleteMapping("/remove/{productVariantId}")
    public ResponseEntity<ResponseSuccess<CartResponse>> removeProductFromCart(
            @PathVariable Long productVariantId
    ) {
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Remove product from cart success",
                cartService.removeProduct( productVariantId)
        ));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ResponseSuccess<Void>> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(new ResponseSuccess<>(
                OK,
                "Clear cart success",
                null
        ));
    }
}
