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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.Scaliveswiftcart.dto.request.UpdateUserRequestDTO;
import in.Scaliveswiftcart.dto.request.UserRequestDTO;
import in.Scaliveswiftcart.dto.response.ApiResponseDTO;
import in.Scaliveswiftcart.dto.response.UserResponseDTO;
import in.Scaliveswiftcart.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
	private UserService serv;
	
	@Autowired
   public UserController(UserService serv) {
		this.serv = serv;
	}
   @PostMapping
   public ResponseEntity<ApiResponseDTO<UserResponseDTO>> createUser(@Valid @RequestBody UserRequestDTO userRequest){
        UserResponseDTO dto= serv.createUser(userRequest);
        ApiResponseDTO<UserResponseDTO> obj = ApiResponseDTO.success("User created successfully", dto); 
        return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }
   @GetMapping("/{id}")
   public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(@PathVariable Long id){
	   UserResponseDTO dto = serv.getUserById(id);
	   ApiResponseDTO<UserResponseDTO> obj = ApiResponseDTO.success(dto);
	   return ResponseEntity.ok(obj);
   }
   @GetMapping("/{email}")
   public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserByEmail(@PathVariable String email){
	   UserResponseDTO dto = serv.getUserByEmail(email);
	   ApiResponseDTO<UserResponseDTO> obj = ApiResponseDTO.success(dto);
	   return ResponseEntity.ok(obj);
   }
   @GetMapping
   public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllUser(){
	   List<UserResponseDTO> users = serv.getAllUser();
	   ApiResponseDTO<List<UserResponseDTO>> obj = ApiResponseDTO.success(users);
	   return ResponseEntity.ok(obj);
   }
   
   @GetMapping("/{active}")
   public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllActiveUser(){
	   List<UserResponseDTO> users = serv.getActiveUser();
	   ApiResponseDTO<List<UserResponseDTO>> obj = ApiResponseDTO.success(users);
	   return ResponseEntity.ok(obj);
   }
   @PutMapping("/{id}")
   public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO userRequest){
	   UserResponseDTO dto = serv.UpdateUser(id, userRequest);
	   ApiResponseDTO<UserResponseDTO> obj = ApiResponseDTO.success("User updated successfully", dto);
	   return ResponseEntity.ok(obj);
   }
   
   @PatchMapping("/{id}/activate")
   public ResponseEntity<ApiResponseDTO<Void>> activateUser( @PathVariable long id){
	   serv.activateUser(id);
	   return ResponseEntity.ok(ApiResponseDTO.success("User activated successfully"));
   }
   
   @PatchMapping("/{id}/deactivate")
   public ResponseEntity<ApiResponseDTO<Void>> deActivateUser( @PathVariable long id){
	   serv.deactivateUser(id);
	   return ResponseEntity.ok(ApiResponseDTO.success("User de-activated successfully"));
   }
   
   @GetMapping("/search")
   public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> searchUsers(@RequestParam String keyword){
	   List<UserResponseDTO> users= serv.searchUser(keyword);
	   ApiResponseDTO<List<UserResponseDTO>> obj = ApiResponseDTO.success(users);
	   return ResponseEntity.ok(obj);
   }
   @GetMapping("/chech-email")
   public ResponseEntity<ApiResponseDTO<Boolean>> checkEmailExists(@RequestParam String email){
	   Boolean res= serv.existsByEmail(email);
	   ApiResponseDTO<Boolean> obj = ApiResponseDTO.success(res?"Email already exists":"Email available for use");
	   return ResponseEntity.ok(obj);
   }
   
}   
