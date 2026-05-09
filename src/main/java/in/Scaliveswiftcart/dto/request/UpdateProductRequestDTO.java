package in.Scaliveswiftcart.dto.request;

import jakarta.validation.constraints.Min;
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
public class UpdateProductRequestDTO {
   
	
	@Size(min =2, max = 200, message = "Product name must be 2 to 200 characters")
    private String name;
	
	@Size(max = 1000, message = "Description must be 1000 characters")
    private String description;
	
	@Min(value = 0, message =  "Price must be grater than or equal to 0")
    private Double price;
	
	@Min(value = 0, message = "Quantity muse be grater than or equal to 0")
    private Integer stockQuantity;
	
	@Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;
	
	@Size(max =100, message = "Brand must  not exceed 100 characters")
	private String brand;
	
	@Size(max= 500, message = "Image url must not exceed 500 characters")
    private String imageUrl;
	
	@Size(max = 50, message = "Sku must not excees 50 characters")
    private String sku;
	
	
	private Boolean isAvailable;
    
    
}
