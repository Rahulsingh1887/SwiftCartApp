package in.Scaliveswiftcart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.Scaliveswiftcart.dto.request.AddToCartRequestDTO;
import in.Scaliveswiftcart.dto.request.UpdateCartItemRequestDTO;
import in.Scaliveswiftcart.dto.response.CartItemResponseDTO;
import in.Scaliveswiftcart.dto.response.CartResponseDTO;
import in.Scaliveswiftcart.entity.Cart;
import in.Scaliveswiftcart.entity.CartItem;
import in.Scaliveswiftcart.entity.Product;
import in.Scaliveswiftcart.exception.InsufficientStockException;
import in.Scaliveswiftcart.exception.ResourceNotFoundException;
import in.Scaliveswiftcart.repository.CartItemRepository;
import in.Scaliveswiftcart.repository.CartRepository;
import in.Scaliveswiftcart.repository.ProductRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    private CartRepository cartRepo;
	private ProductRepository productRepo;
	private CartItemRepository cartItemRepo;

	public CartServiceImpl(CartRepository cartRepo, ProductRepository productRepo, CartItemRepository cartItemRepo) {	
		this.cartRepo = cartRepo;
		this.productRepo = productRepo;
		this.cartItemRepo = cartItemRepo;
	}

	@Override
	public CartResponseDTO getCartByUserId(Long userId) {
		Cart cart = getCart(userId);
		return mapToCartResponseDTO(cart);	
	}

	@Override
	public CartResponseDTO addItemToCart(Long userId, AddToCartRequestDTO dto) {
		Cart cart = getCart(userId);
		Product product= findProductById(dto.getProductId());		
		validateProductAvailability(product, dto.getQuantity());
		
		Optional<CartItem> opt= cartItemRepo.findByCartIdAndProductId(cart.getId(), product.getId());
		if(opt.isPresent()) {
			CartItem cartItem = opt.get();
			int newQty =  cartItem.getQuantity() + dto.getQuantity();
			validateProductAvailability(product, newQty);
			cartItem.setQuantity(newQty);
			cartItem.calculateSubTotal();
		}else {
			CartItem cartItem = CartItem.builder()
					.product(product)
					.quantity(dto.getQuantity())
					.unitPrice(product.getPrice())
					.build();
			
			cartItem.calculateSubTotal();
			cart.addCartItem(cartItem);
		}
		cartRepo.save(cart);
		return mapToCartResponseDTO(cart);			
	}

	@Override
	public CartResponseDTO updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequestDTO dto) {
		Cart cart = getCart(userId);		
		CartItem cartItem = findCartItemById(cartItemId);
		if(!cartItem.getCart().getId().equals(cart.getId())) {
			throw new ResourceNotFoundException("CartItem","id", cartItem);
		}
		validateProductAvailability(cartItem.getProduct(), dto.getQuantity());
		cartItem.setQuantity(dto.getQuantity());
		cartItem.calculateSubTotal();
		cart.recalculateTotals();
		cartRepo.save(cart);
		return mapToCartResponseDTO(cart);
		
	}

	@Override
	public CartResponseDTO removeItemFromCart(Long userId, Long cartItemId) {
		Cart cart = getCart(userId);
		CartItem cartItem = findCartItemById(cartItemId);
		if(!cartItem.getCart().getId().equals(cart.getId())) {
			throw new ResourceNotFoundException("CartItem","id", cartItem);
		}
		cart.removeCartItem(cartItem);
		cart.recalculateTotals();
		cartRepo.save(cart);
		return mapToCartResponseDTO(cart);
	}

	@Override
	public CartResponseDTO ClearCart(Long userId) {
		Cart cart = getCart(userId);
		cart.clearCart();
		cartRepo.save(cart);
		return mapToCartResponseDTO(cart);
	}

	@Override
	public CartItemResponseDTO getCartItem(Long userId, Long cartItemId) {
		Cart cart = getCart(userId);
		CartItem cartItem = findCartItemById(cartItemId);
		if(!cartItem.getCart().getId().equals(cart.getId())) {
			throw new ResourceNotFoundException("CartItem","id", cartItem);
		}
		return mapToCartItemResponseDTO(cartItem);
	}

	@Override
	public Boolean isProductInCart(Long userId, Long productId) {
		Optional<Cart> opt	= cartRepo.findById(userId);
		if(!opt.isPresent()) {
			return false;
		}
		Cart cart = opt.get();
		return cartItemRepo.existsByCartIdAndProductId(cart.getId(), productId);
	}

	@Override
	public CartResponseDTO incrementItemQuantity(Long userId, Long productId, int quantity) {
		if(quantity < 0) {
			throw new IllegalArgumentException("Quantity must be positive");
		}
		Cart cart = getCart(userId);
		Optional<CartItem> opt=cartItemRepo.findByCartIdAndProductId(cart.getId(), productId);
		if(!opt.isPresent()) {
			throw new ResourceNotFoundException("cartItem", "productId", productId);
		}
		CartItem cartItem = opt.get();
		int newQty =  cartItem.getQuantity() + quantity;
		validateProductAvailability(cartItem.getProduct(), newQty);
		cartItem.setQuantity(newQty);
		cartItem.calculateSubTotal();
		cart.recalculateTotals();
	    cartRepo.save(cart);	
		return mapToCartResponseDTO(cart);
	}

	@Override
	public CartResponseDTO decrementItemQuantity(Long userId, Long productId, int quantity) {
		if(quantity < 0) {
			throw new IllegalArgumentException("Quantity must be positive");
		}
		Cart cart = getCart(userId);
		Optional<CartItem> opt=cartItemRepo.findByCartIdAndProductId(cart.getId(), productId);
		if(!opt.isPresent()) {
			throw new ResourceNotFoundException("cartItem", "productId", productId);
		}
		CartItem cartItem = opt.get();
		int newQty =  cartItem.getQuantity() - quantity;
		if(newQty < 0) {
			cart.removeCartItem(cartItem);
		}else {
			cartItem.setQuantity(newQty);
			cartItem.calculateSubTotal();
		}
		cart.recalculateTotals();
	    cartRepo.save(cart);	
		return mapToCartResponseDTO(cart);
		
	}
	
	private Cart getCart(Long userId) {
		Optional<Cart> opt = cartRepo.findByUserId(userId);
		if(opt.isEmpty()) {
			throw new ResourceNotFoundException("Cart", "userId", userId);
		}
		return opt.get();
	}
	
	private Product findProductById(Long prodId) {
		Optional<Product> opt = productRepo.findById(prodId);
		if(opt.isEmpty()) {
			throw new ResourceNotFoundException("Cart", "productId", prodId);
		}
		return opt.get();
	}
	
	private void validateProductAvailability (Product product, int requestQty) {
		if(!product.getIsAvailable()) {
		throw new InsufficientStockException(product.getName()+" is not available");
		}
		if(product.getStockQuantity()<requestQty) {
		throw new InsufficientStockException("Insufficient stock for "+
		product.getName()+
		". Available:"+product.getStockQuantity()+
		", Requested: "+requestQty);
		  }
		}
	
   private CartItem findCartItemById(Long cartItemId) {
	   Optional<CartItem> opt = cartItemRepo.findById(cartItemId);
		if(opt.isEmpty()) {
			throw new ResourceNotFoundException("Cart", "cartItemId", cartItemId);
		}
		return opt.get();
   }
   
   
   private CartResponseDTO mapToCartResponseDTO(Cart cart) {
	   List<CartItemResponseDTO>items=new ArrayList<>();
	   for(CartItem item:cart.getCartItems()) {
		   items.add(mapToCartItemResponseDTO(item));
	   }
       return CartResponseDTO.builder()
	   .id(cart.getId())
	   .userId(cart.getUser().getId())
	   .userName(cart.getUser().getFullName())
	   .items(items)
	   .totalItems(cart.getTotalItems())
	   .totalAmount(cart.getTotalAmount())
	   .createdAt(cart.getCreatedAt())
	   .updatedAt(cart.getUpdatedAt())
	   .build();
   }
   private CartItemResponseDTO mapToCartItemResponseDTO(CartItem item) {
	   Product product=item.getProduct();
	   return CartItemResponseDTO.builder()
	   .id(item.getId())
	   .productId(product.getId())
	   .productName(product.getName())
	   .productImage(product.getImageUrl())
	   .productSku(product.getSku())
	   .unitprice(product.getPrice())
	   .quantity(item.getQuantity())
	   .subTotal(item.getSubTotal())
	   .availableStock(product.getStockQuantity())
	   .addedAt(item.getAddedTime())
	   .build();
   }
}
