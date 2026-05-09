package in.Scaliveswiftcart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class PlaceOrederRequestDTO {
	@NotNull(message = "User id is required")
   private Long userId;
   
   @Size(max = 500, message = "Notes must not exceed more than 500 charrecter")
   private String notes;
   
   
}
