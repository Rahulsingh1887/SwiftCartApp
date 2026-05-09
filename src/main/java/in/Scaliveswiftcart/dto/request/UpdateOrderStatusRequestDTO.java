package in.Scaliveswiftcart.dto.request;

import jakarta.validation.constraints.NotNull;
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
public class UpdateOrderStatusRequestDTO {
   
	@NotNull(message = "Order status is required")
	private String orderStatus;
	
	private String notes;
}
