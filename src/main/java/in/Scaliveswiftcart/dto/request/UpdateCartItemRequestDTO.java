package in.Scaliveswiftcart.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

public class UpdateCartItemRequestDTO {
	
    @NotNull(message = "Quantity is required")
	@Min(value = 1 , message = "Min Quantity required must be 1")
	@Max(value = 100, message = "Maxmum quantity can be 100")
   private Integer quantity;
}
