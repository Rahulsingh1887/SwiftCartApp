package in.Scaliveswiftcart.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
	@NotBlank(message = "Please enter your full name.")
	@Size(min = 2, max=100, message = "Your full name should be between 2 and 100 characters.")
    private String fullName;
	
	@NotBlank(message = "Please enter your Email id.")
	@Email(message = "Please enter valid Email")
	@Size(max=150, message = "Your Email should be less than 150 characters.")
    private String email;
	
	@NotBlank(message = "Please enter your Password.")
	@Size(min = 8, max=100, message = "Your password should be between 8 and 100 characters.")
    private String password;
	
	@Pattern(regexp = "[0-9]{10}$", message= "Phone no must be exactly 10 digits only.")
    private String phone;
	@Size(max = 150, message = " Address does not exceed 150 charecters.")
    private String address;
}
