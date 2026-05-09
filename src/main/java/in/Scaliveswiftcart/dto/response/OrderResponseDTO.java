package in.Scaliveswiftcart.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.LongToDoubleFunction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
   private Long id;
   private String orderNumber;
   private Long userId;
   private String userName;
   private String userEmail;
   private List<OrderItemResponseDTO> items;
   private String status;
   private String notes;
   private LocalDateTime orderDate;
}
