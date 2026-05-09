package in.Scaliveswiftcart.dto.response;

import java.time.LocalDateTime;

import in.Scaliveswiftcart.dto.request.UpdateCartItemRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDTO {
   private Long id;
   private Long productId;
   private String productName;
   private String productImage;
   private String productSku;
   private Double unitprice;
   private Integer quantity;
   private double subTotal;
   private Integer availableStock;
   private LocalDateTime addedAt;
}
