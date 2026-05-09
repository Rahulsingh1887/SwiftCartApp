package in.Scaliveswiftcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.Scaliveswiftcart.dto.request.AddToCartRequestDTO;
import in.Scaliveswiftcart.dto.request.UpdateCartItemRequestDTO;
import in.Scaliveswiftcart.dto.response.ApiResponseDTO;
import in.Scaliveswiftcart.dto.response.CartItemResponseDTO;
import in.Scaliveswiftcart.dto.response.CartResponseDTO;
import in.Scaliveswiftcart.service.CartService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin("*")
public class CartController {
	
	private CartService serv;
	
	@Autowired
	public CartController(CartService serv) {
		this.serv = serv;
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> getCart(@PathVariable Long userId){
		CartResponseDTO dto = serv.getCartByUserId(userId);
		return  ResponseEntity.ok(ApiResponseDTO.success(dto));
	}
	
	@PostMapping("/{userId}/items")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> addItemToCart(@PathVariable Long userId, @Valid @RequestBody AddToCartRequestDTO dto){
		CartResponseDTO cartDto = serv.addItemToCart(userId, dto);
		return ResponseEntity.ok(ApiResponseDTO.success("Item added successfully ",cartDto));
	}
	
	@PutMapping("/{userid}/items/{cartItemId}")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> updateCartItem(@PathVariable Long userId,@PathVariable  Long cartItemId, @Valid @RequestBody UpdateCartItemRequestDTO dto){
		CartResponseDTO updateDto  = serv.updateCartItem(userId, cartItemId, dto);
		return ResponseEntity.ok(ApiResponseDTO.success("Cart updated successfully", updateDto));
	}
	
	@DeleteMapping("/{userId}/item/{cartItemId}")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> removeItemFromCart(@PathVariable Long userId, @PathVariable Long cartitemId){
		CartResponseDTO dto = serv.removeItemFromCart(userId, cartitemId);
		return  ResponseEntity.ok(ApiResponseDTO.success("Cart Removed successfully", dto));
	}

	@DeleteMapping("/{userId}/clear")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> clearCart(@PathVariable Long userId){
		CartResponseDTO dto = serv.ClearCart(userId);
		return  ResponseEntity.ok(ApiResponseDTO.success("Cart Cleared Successfully ", dto));
	}
	
	@GetMapping("/{userId}/item/{cartItemId}")
	public ResponseEntity<ApiResponseDTO<CartItemResponseDTO>> getCartItem(@PathVariable Long userId, @PathVariable Long cartItemId){
		CartItemResponseDTO dto = serv.getCartItem(userId, cartItemId);
		return ResponseEntity.ok(ApiResponseDTO.success(dto));
	}
	
	@GetMapping("/{userId}/check-product/{productId}")
	public ResponseEntity<ApiResponseDTO<Boolean>> isProductInCart(@PathVariable Long userId, @PathVariable Long productId){
		boolean inCart =  serv.isProductInCart(userId, productId);
		return ResponseEntity.ok(ApiResponseDTO.success(inCart? "Product is in cart":"Product is not in the Cart", inCart));
	}
	
	@PatchMapping("/{userId}/products/{productId}/increment")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> incrementItemQuantity(@PathVariable Long userId, @PathVariable Long productId, @RequestParam(defaultValue = "1") int quantity){
		CartResponseDTO dto = serv.incrementItemQuantity(userId, productId, quantity);
		return ResponseEntity.ok(ApiResponseDTO.success("Quantity incremented Successfully ", dto));
	}
	
	@PatchMapping("/{userId}/products/{productId}/decrement")
	public ResponseEntity<ApiResponseDTO<CartResponseDTO>> decrementItemQuantity(@PathVariable Long userId, @PathVariable Long productId, @RequestParam(defaultValue = "1") int quantity){
		CartResponseDTO dto = serv.decrementItemQuantity(userId, productId, quantity);
		return ResponseEntity.ok(ApiResponseDTO.success("Quantity decremented Successfully ",dto));
	}
}
