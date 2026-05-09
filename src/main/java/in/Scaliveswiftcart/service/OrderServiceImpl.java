package in.Scaliveswiftcart.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.Scaliveswiftcart.dto.request.PlaceOrederRequestDTO;
import in.Scaliveswiftcart.dto.request.UpdateOrderStatusRequestDTO;
import in.Scaliveswiftcart.dto.response.OrderItemResponseDTO;
import in.Scaliveswiftcart.dto.response.OrderResponseDTO;
import in.Scaliveswiftcart.dto.response.PageResponseDTO;
import in.Scaliveswiftcart.entity.Cart;
import in.Scaliveswiftcart.entity.CartItem;
import in.Scaliveswiftcart.entity.Order;
import in.Scaliveswiftcart.entity.OrderItem;
import in.Scaliveswiftcart.entity.Product;
import in.Scaliveswiftcart.entity.User;
import in.Scaliveswiftcart.exception.EmptyCartException;
import in.Scaliveswiftcart.exception.InsufficientStockException;
import in.Scaliveswiftcart.exception.InvalidOprationException;
import in.Scaliveswiftcart.exception.ResourceNotFoundException;
import in.Scaliveswiftcart.repository.CartRepository;
import in.Scaliveswiftcart.repository.OrderItemRepository;
import in.Scaliveswiftcart.repository.OrderRepository;
import in.Scaliveswiftcart.repository.ProductRepository;
import in.Scaliveswiftcart.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService{

	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final CartRepository cartRepo;
	private final UserRepository userRepo;
	private final ProductRepository proRepo;
	
	@Autowired
	public OrderServiceImpl(OrderRepository orderRepo, OrderItemRepository orderItemRepo, CartRepository cartRepo,
		UserRepository userRepo, ProductRepository proRepo) {
		this.orderRepo = orderRepo;
		this.orderItemRepo = orderItemRepo;
		this.cartRepo = cartRepo;
		this.userRepo = userRepo;
		this.proRepo = proRepo;
	}

	
	@Override
	@Transactional
	public OrderResponseDTO placeOrder(PlaceOrederRequestDTO dto) {
		Optional<User> userOptional = userRepo.findById(dto.getUserId());
		if (!userOptional.isPresent()) {
			throw new ResourceNotFoundException("User", "id", dto.getUserId());
		}		
		User user = userOptional.get();
		Optional<Cart> cartoptional = cartRepo.findByUserIdWithItems (dto.getUserId());

		if (!cartoptional.isPresent()) {
			throw new ResourceNotFoundException("Cart", "userId", dto.getUserId());
		}
		Cart cart = cartoptional.get();
		if (cart.getCartItems().isEmpty()) {
		throw new EmptyCartException("Cannot place order with an empty cart");
		}
		
		List<CartItem> cartItemsCopy = new ArrayList<>(cart.getCartItems());
		for (CartItem cartItem: cartItemsCopy) {
		Product product = cartItem.getProduct();
		if (!product.getIsAvailable()) {
		throw new InsufficientStockException (product.getName() + "is no longer available");
		 }
		
		if (product.getStockQuantity() < cartItem.getQuantity()) {
		throw new InsufficientStockException("Insufficient stock for " + product.getName() + "Available:" + product.getStockQuantity() +  "Requested:" + cartItem.getQuantity());
		 }
		}

		double totalAmount = 0.0;
		for (CartItem cartItem: cartItemsCopy) {
			totalAmount = totalAmount + cartItem.getSubTotal();
		}
// create order
		Order order = Order.builder()
        		.user(user)
                .totalAmount(totalAmount)
         		.notes(dto.getNotes())
		        .status(Order.STATUS_CONFIRMED)
		        .build();
	        
		   Order savedOrder = orderRepo.save(order);
		   
		   List<OrderItem> orderItems = new ArrayList<>();

	   for (CartItem cartItem: cartItemsCopy) {
		   OrderItem orderItem = OrderItem.fromCartItem(cartItem);
		   orderItem.setOrder(savedOrder);
		   orderItems.add(orderItem);
		   proRepo.decreaseStock(cartItem.getProduct().getId(), cartItem.getQuantity());
		   } 
	       orderItemRepo.saveAll(orderItems);
		   savedOrder.setOrderItems(orderItems);
		   
		   // Clear the cart after order is placed
		   cart.getCartItems().clear();
		   cart.setTotalItems(0);
		   cart.setTotalAmount(0.0);
		   cartRepo.save(cart);

		   return mapToOrderResponseDTO(savedOrder);		   
	}

	@Override
	public OrderResponseDTO getOrderById(Long orderId) {
		Order order = findOrderById(orderId);
		return mapToOrderResponseDTO(order);
	}

	@Override
	public OrderResponseDTO getOrderByOrderNumber(String orderNumber) {
		Optional<Order> orderOptional = orderRepo.findByOrderNumberWithItems (orderNumber);
		if (orderOptional.isPresent()) {
		 return mapToOrderResponseDTO (orderOptional.get());
		} else {
		throw new ResourceNotFoundException("Order", "orderNumber", orderNumber);
		}
		}

	@Override
	public List<OrderResponseDTO> getOrderByUserId(Long userId) {
		List<Order> orders = orderRepo.findByUserIdOrderByOrderDateDesc(userId);
		List<OrderResponseDTO> responseList = new ArrayList<>();
		for (Order order: orders) {
		responseList.add(mapToOrderResponseDTO(order));
		}
	
		return responseList;
	}

	@Override
	public PageResponseDTO<OrderResponseDTO> getAllOrdersPaginated(int page, int size, String sortBy, String sortDir) {
		
		Sort sort;
		if (sortDir.equalsIgnoreCase("desc")) {
		sort = Sort.by(sortBy).descending();
		} else {
			sort = Sort.by(sortBy).ascending();
		}	
		 Pageable pageable = PageRequest.of(page, size, sort);
		Page<Order> orderPage = orderRepo.findAll(pageable);
		return mapToPageResponse(orderPage);
	}

	@Override
	public List<OrderResponseDTO> getOrderByStatus(String status) {	
		List<Order> orders = orderRepo.findByStatus(status);
		List<OrderResponseDTO> responseList = new ArrayList<>();		
		for (Order order: orders) {
		responseList.add(mapToOrderResponseDTO(order));
		}
		return responseList;
	}

	@Override
	public OrderResponseDTO UpdateOrderStatus(Long orderId, UpdateOrderStatusRequestDTO dto) {
		Order order = findOrderById(orderId);
		validateStatusTransition(order.getStatus(), dto.getOrderStatus());
		order.setStatus(dto.getOrderStatus());
		if (Order.STATUS_CANCELLED.equals(dto.getOrderStatus())){ 
			restoreStock(order);
		}
		if (dto.getNotes() != null && !dto.getNotes().isEmpty()) {
		String existingllotes = order.getNotes() != null? order.getNotes() + "\n" : "";
		order.setNotes(existingllotes + "[" + LocalDateTime.now() + "] " + dto.getNotes());
		}
		Order updatedOrder = orderRepo.save(order); return mapToOrderResponseDTO(updatedOrder);
	}

	@Override
	public OrderResponseDTO cancelOrder(Long orderId, String reason) {
		Order order = findOrderById(orderId);
		if (!Order.STATUS_CONFIRMED.equals(order.getStatus())) {
			throw new InvalidOprationException("Cannot cancel order with status:" + order.getStatus());
		}		

		order.setStatus(Order.STATUS_CANCELLED);
		String notes = order.getNotes() != null? order.getNotes() + "\n" : "";
		order.setNotes(notes + "[" + LocalDateTime.now() + "] Cancelled: " + reason);
		restoreStock(order);
		Order cancelledOrder = orderRepo.save(order);
		return mapToOrderResponseDTO(cancelledOrder);		
	}

	@Override
	public PageResponseDTO<OrderResponseDTO> searchOrders(String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Order> orderPage = orderRepo.SearchOrder(keyword, pageable);
		return mapToPageResponse(orderPage);
	}
	
	// helper methods
	private Order findOrderById(Long orderId) {
	 Optional<Order> orderOptional = orderRepo.findById(orderId);
	 if(orderOptional.isPresent()) {
	  return orderOptional.get();
	  } else {
	    throw new ResourceNotFoundException("Order", "id", orderId);
	    }
	}
	
	private void validateStatusTransition(String currentStatus, String newStatus) {
	if (Order.STATUS_CONFIRMED.equals(currentStatus)) {
	if (!Order.STATUS_CANCELLED.equals(newStatus)) {
		throw new InvalidOprationException("Cannot transition from CONFIRMED to " + newStatus);
	  }
	}else if (Order.STATUS_CANCELLED.equals(currentStatus)) {
		throw new InvalidOprationException("Cannot change status of "+ currentStatus + "order");
     	}
	  
	}
	
	private void restoreStock(Order order) {
		for(OrderItem orderItem: order.getOrderItems()) {
			proRepo.increaseStock(orderItem.getProduct().getId(), orderItem.getQuantity());
		}
	}
	
	private OrderResponseDTO mapToOrderResponseDTO(Order order) {
		List<OrderItemResponseDTO> items = new ArrayList<>();
		for (OrderItem orderItem: order.getOrderItems()) {
		items.add(mapToOrderItemResponseDTO(orderItem));
		}
		int totalItems = 0;
		for (OrderItem orderItem: order.getOrderItems()) {  totalItems = totalItems + orderItem.getQuantity();
		}
		return OrderResponseDTO.builder()
		.id(order.getId())
		.orderNumber(order.getOrderNumber())
		.userId(order.getUser().getId())
		.userName(order.getUser().getFullName())
		.userEmail(order.getUser().getEmail())
		.items(items)
		.status(order.getStatus())
		.notes(order.getNotes())
		.orderDate(order.getOrderDate())
		.build();
	}
	private OrderItemResponseDTO mapToOrderItemResponseDTO (OrderItem orderItem) {
		return OrderItemResponseDTO.builder()
		.id(orderItem.getId())
		.productId(orderItem.getProduct().getId())
		.productName(orderItem.getProductName())
		.productSku(orderItem.getProductSku())
		.productImage(orderItem.getProduct().getImageUrl())
		.quantity(orderItem.getQuantity())
		.unitPrice(orderItem.getUnitPrice())
		.subTotal(orderItem.getSubTotal())
		.build();
	}
	
	private PageResponseDTO<OrderResponseDTO> mapToPageResponse(Page<Order> orderPage) {

		List<OrderResponseDTO> orders = new ArrayList<>();
		for (Order order: orderPage.getContent()) {
			orders.add(mapToOrderResponseDTO (order));
		}
	
		return PageResponseDTO.<OrderResponseDTO>builder()
		.content(orders)
		.pageNumber(orderPage.getNumber())
		.pageSize(orderPage.getSize())
		.totalElements(orderPage.getTotalElements())
		.totalPages (orderPage.getTotalPages())
		.first(orderPage.isFirst())
		.last(orderPage.isLast())
		.hasNext(orderPage.hasNext())
		.hasPrivious(orderPage.hasPrevious())
		.build();
		}
}

	