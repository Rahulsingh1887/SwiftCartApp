package in.Scaliveswiftcart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.Scaliveswiftcart.dto.request.PlaceOrederRequestDTO;
import in.Scaliveswiftcart.dto.request.UpdateOrderStatusRequestDTO;
import in.Scaliveswiftcart.dto.response.ApiResponseDTO;
import in.Scaliveswiftcart.dto.response.OrderResponseDTO;
import in.Scaliveswiftcart.dto.response.PageResponseDTO;
import in.Scaliveswiftcart.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
   private OrderService serv;
    @Autowired
   public OrderController(OrderService serv) {
	this.serv = serv;
   }
   
    @PostMapping
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> placeOrder(@Valid @RequestBody PlaceOrederRequestDTO dto) {
     OrderResponseDTO order = serv.placeOrder(dto);
     return new ResponseEntity<>(ApiResponseDTO.success("Order placed successfully", order),HttpStatus.CREATED);
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> getOrderById(@PathVariable Long orderId) {
    	 OrderResponseDTO order = serv.getOrderById(orderId);
    	    return ResponseEntity.ok(ApiResponseDTO.success(order));
    }
    
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> getOrderByOrderNumber(@PathVariable String orderNumber){
    	OrderResponseDTO order = serv.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(ApiResponseDTO.success (order));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO<List<OrderResponseDTO>>> getOrdersByUserId(@PathVariable Long userId) {
      List<OrderResponseDTO> orders = serv.getOrderByUserId(userId);
        return ResponseEntity.ok(ApiResponseDTO.success("Fetched " + orders.size() + " orders", orders)); 
    }
     @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<OrderResponseDTO>>>getAllOrdersPaginated(
    		@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size,
    		@RequestParam(defaultValue = "orderDate") String sortBy,
    		@RequestParam(defaultValue = "desc") String sortDir) {
    		PageResponseDTO<OrderResponseDTO> orders = serv.getAllOrdersPaginated(page, size, sortBy, sortDir);
    		return ResponseEntity.ok(ApiResponseDTO.success(orders));
    		}

     @GetMapping("/status/{status}")
     public ResponseEntity<ApiResponseDTO<List<OrderResponseDTO>>> getOrdersByStatus(
     @PathVariable String status) {
     List<OrderResponseDTO> orders = serv.getOrderByStatus(status);
     return ResponseEntity.ok(ApiResponseDTO.success(orders));
     }
     
     @PatchMapping("/{orderId}/status")
     public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> updateOrderStatus(
     @PathVariable Long orderId,
     @Valid @RequestBody UpdateOrderStatusRequestDTO updateOrderStatusRequestDTO) {
     OrderResponseDTO order = serv.UpdateOrderStatus (orderId, updateOrderStatusRequestDTO);
     return ResponseEntity.ok(ApiResponseDTO.success("Order status updated successfully", order));
     }
     
     @PostMapping("/{orderId}/cancel")
     public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> cancelOrder( @PathVariable Long orderId,
    	   @RequestParam(required = false, defaultValue = "Customer requested cancellation") String reason){
       OrderResponseDTO order = serv.cancelOrder(orderId, reason);
      return ResponseEntity.ok(ApiResponseDTO.success("Order cancelled successfully", order));
    }
    
     @GetMapping("/search")
     public ResponseEntity<ApiResponseDTO<PageResponseDTO<OrderResponseDTO>>> searchOrders(
    		 @RequestParam String keyword,
        	 @RequestParam(defaultValue = "0") int page,
        	 @RequestParam(defaultValue = "10") int size) {
        	 PageResponseDTO<OrderResponseDTO> orders = serv.searchOrders (keyword, page, size);
        	 return ResponseEntity.ok(ApiResponseDTO.success (orders));       
    	 }
    	 
}
