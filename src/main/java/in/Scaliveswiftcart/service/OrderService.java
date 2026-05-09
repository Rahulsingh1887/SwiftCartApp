package in.Scaliveswiftcart.service;

import java.util.List;

import in.Scaliveswiftcart.dto.request.PlaceOrederRequestDTO;
import in.Scaliveswiftcart.dto.request.UpdateOrderStatusRequestDTO;

import in.Scaliveswiftcart.dto.response.OrderResponseDTO;
import in.Scaliveswiftcart.dto.response.PageResponseDTO;

public interface OrderService {
  OrderResponseDTO placeOrder(PlaceOrederRequestDTO dto);
  OrderResponseDTO getOrderById(Long orderId);
  OrderResponseDTO getOrderByOrderNumber(String orderNumber);
  List<OrderResponseDTO> getOrderByUserId(Long userId);
  PageResponseDTO<OrderResponseDTO> getAllOrdersPaginated(int page, int size, String sortBy, String sortDir);
  List<OrderResponseDTO> getOrderByStatus(String status);
  OrderResponseDTO UpdateOrderStatus(Long orderId,UpdateOrderStatusRequestDTO dto);
  OrderResponseDTO cancelOrder(Long orderId, String reason);
  PageResponseDTO<OrderResponseDTO> searchOrders(String keyword, int page, int size);
}
