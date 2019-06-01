package rocks.danielw.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import rocks.danielw.model.entities.UserEntity;
import rocks.danielw.web.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

  UserDto createUser(UserDto userDto);

  UserDto getUserByUserId(String userId);

  Optional<UserDto> getUserByEmail(String email);

  UserDto updateUser(String userId, UserDto userDto);

  void deleteUser(String userId);

  List<UserDto> getAllUsers();

  List<UserDto> getAllUsers(int page, int limit);

  void verifyEmailToken(String tokenString);

  void changePassword(UserEntity userEntity, String newPassword);
	
}
