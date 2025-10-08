package vn.com.ecomstore.services.impl;

import vn.com.ecomstore.dtos.request.cart.CartAddRequest;
import vn.com.ecomstore.dtos.response.cart.CartResponse;
import vn.com.ecomstore.entities.Cart;
import vn.com.ecomstore.entities.CartDetail;
import vn.com.ecomstore.entities.ProductVariant;
import vn.com.ecomstore.entities.User;
import vn.com.ecomstore.exceptions.custom.ResourceNotFoundException;
import vn.com.ecomstore.mappers.CartMapper;
import vn.com.ecomstore.repositories.CartRepository;
import vn.com.ecomstore.repositories.ProductVariantRepository;
import vn.com.ecomstore.services.CartService;
import vn.com.ecomstore.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final SecurityUtil securityUtil;
    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartMapper cartMapper;

    @Override
    public CartResponse getOrCreateCart() {
        return cartMapper.toResponse(findOrCreateCartForCurrentUser());
    }

    @Override
    public CartResponse addProduct(CartAddRequest request) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        Cart cart = findOrCreateCartForCurrentUser();
        ProductVariant productVariant = findProductVariant(request.getProductVariantId());
        if (request.getQuantity() > productVariant.getStock()) {
            throw new IllegalArgumentException("Quantity exceeds available stock");
        }
        CartDetail cartDetail = findCartDetail(cart, productVariant);
        if (cartDetail != null) {
            long newQuantity = cartDetail.getQuantity() + request.getQuantity();
            if (newQuantity > productVariant.getStock()) {
                throw new IllegalArgumentException("Total quantity in cart exceeds available stock");
            }
            if (newQuantity <= 0) {
                cart.getCartDetails().remove(cartDetail);
            } else {
                updateCartDetailQuantityAndPrice(cartDetail, request.getQuantity(), productVariant.getPrice());
            }
        } else {
            addNewCartDetail(cart, productVariant, request.getQuantity());
        }
        updateCartTotalItems(cart);
        cartRepository.save(cart);
        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse removeProduct(Long productVariantId) {
        Cart cart = findOrCreateCartForCurrentUser();
        ProductVariant productVariant = findProductVariant(productVariantId);
        CartDetail cartDetail = findCartDetail(cart, productVariant);
        if (cartDetail != null) {
            cart.getCartDetails().remove(cartDetail);
        }
        updateCartTotalItems(cart);
        cartRepository.save(cart);
        return cartMapper.toResponse(cart);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        cart.getCartDetails().clear();
        cart.setTotalItems(0L);

        cartRepository.save(cart);
    }

    private Cart findOrCreateCartForCurrentUser() {
        User user = securityUtil.getCurrentUser();
        return cartRepository.findByUser_Id(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalItems(0L);
                    return cartRepository.save(newCart);
                });
    }

    private ProductVariant findProductVariant(Long productVariantId) {
        return productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new ResourceNotFoundException("ProductVariant not found"));
    }

    private CartDetail findCartDetail(Cart cart, ProductVariant productVariant) {
        return cart.getCartDetails().stream()
                .filter(cd -> cd.getProductVariant().getId().equals(productVariant.getId()))
                .findFirst()
                .orElse(null);
    }

    private void updateCartDetailQuantityAndPrice(CartDetail cartDetail, int addedQuantity, double pricePerUnit) {
        long newQuantity = cartDetail.getQuantity() + addedQuantity;
        cartDetail.setQuantity(newQuantity);
        cartDetail.setPrice(newQuantity * pricePerUnit);
    }

    private void addNewCartDetail(Cart cart, ProductVariant productVariant, int quantity) {
        CartDetail cartDetail = CartDetail.builder()
                .cart(cart)
                .productVariant(productVariant)
                .quantity((long) quantity)
                .price(productVariant.getPrice() * quantity)
                .build();
        cart.getCartDetails().add(cartDetail);
    }

    private void updateCartTotalItems(Cart cart) {
        long totalItems = cart.getCartDetails().stream()
                .mapToLong(CartDetail::getQuantity)
                .sum();
        cart.setTotalItems(totalItems);
    }


}
