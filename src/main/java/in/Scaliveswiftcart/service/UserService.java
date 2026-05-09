package in.Scaliveswiftcart.service;

import java.util.List;

import in.Scaliveswiftcart.dto.request.UpdateUserRequestDTO;
import in.Scaliveswiftcart.dto.request.UserRequestDTO;
import in.Scaliveswiftcart.dto.response.UserResponseDTO;

public interface UserService {
     UserResponseDTO createUser(UserRequestDTO userRequest); 
     UserResponseDTO getUserById(Long id);
     UserResponseDTO getUserByEmail(String email);
     List<UserResponseDTO> getAllUser();
     List<UserResponseDTO> getActiveUser();
     UserResponseDTO UpdateUser(Long id, UpdateUserRequestDTO userRequest);
     void activateUser(Long id);
     void deactivateUser(Long id);
     List<UserResponseDTO>searchUser(String keyword);
     boolean existsByEmail(String email);
     
     
}
