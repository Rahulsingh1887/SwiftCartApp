package in.Scaliveswiftcart.service;

import in.Scaliveswiftcart.dto.request.AddToCartRequestDTO;
import in.Scaliveswiftcart.dto.request.UpdateCartItemRequestDTO;
import in.Scaliveswiftcart.dto.response.CartItemResponseDTO;
import in.Scaliveswiftcart.dto.response.CartResponseDTO;

public interface CartService {
     CartResponseDTO getCartByUserId(Long userId);
     CartResponseDTO addItemToCart(Long userId, AddToCartRequestDTO addrequest);
     CartResponseDTO updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequestDTO updaterequest);
     CartResponseDTO removeItemFromCart(Long userId, Long cartItemId);
     CartResponseDTO  ClearCart(Long userId);
     CartItemResponseDTO getCartItem(Long userIdLong, Long cartItemId);
     Boolean isProductInCart(Long userId, Long productId);
     CartResponseDTO incrementItemQuantity(Long userId, Long productId, int quantity);
     CartResponseDTO decrementItemQuantity(Long userId, Long ProductId, int quantity);
     

}   
