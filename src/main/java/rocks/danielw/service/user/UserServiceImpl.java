package rocks.danielw.service.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.danielw.model.entities.TokenEntity;
import rocks.danielw.model.entities.UserEntity;
import rocks.danielw.model.exceptions.EmailVerificationTokenExpiredException;
import rocks.danielw.model.exceptions.ErrorMessages;
import rocks.danielw.model.exceptions.ResourceNotFoundException;
import rocks.danielw.model.exceptions.UserAlreadyExistsException;
import rocks.danielw.model.repositories.TokenRepository;
import rocks.danielw.model.repositories.UserRepository;
import rocks.danielw.utils.Utils;
import rocks.danielw.web.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository  userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenRepository tokenRepository;
  private final ModelMapper     mapper;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
    this.userRepository  = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenRepository = tokenRepository;
    this.mapper          = new ModelMapper();
  }

  @Override
  @Transactional
  public UserDto createUser(UserDto userDto) {
    checkIfUserAlreadyExists(userDto.getEmail());

    // enhance information in AddressDto
    userDto.getAddresses().forEach(addressDto -> {
      addressDto.setUserDetails(userDto);
      addressDto.setAddressId(Utils.generatePublicId(10));
    });

    // enhance UserEntity
    UserEntity userEntity = mapper.map(userDto, UserEntity.class);
    userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
    userEntity.setUserId(Utils.generatePublicId(30));

    // create user
    UserEntity savedUserEntity = userRepository.save(userEntity);

    return mapper.map(savedUserEntity, UserDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto getUserByUserId(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId)
                                          .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_WITH_ID_NOT_FOUND.toDisplayString()));

    return mapper.map(userEntity, UserDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UserDto> getUserByEmail(String email) {
    Optional<UserEntity> userEntity = userRepository.findByEmail(email);

    if (! userEntity.isPresent()) {
      return Optional.empty();
    }

    UserDto userDto = mapper.map(userEntity.get(), UserDto.class);
    return Optional.of(userDto);
  }

  @Override
  @Transactional
  public UserDto updateUser(String userId, UserDto userDto) {
    UserEntity userEntity = userRepository.findByUserId(userId)
                                          .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_WITH_ID_NOT_FOUND.toDisplayString()));

    userEntity.setFirstName(userDto.getFirstName());
    userEntity.setLastName(userDto.getLastName());
    UserEntity updatedUserEntity = userRepository.save(userEntity);

    return mapper.map(updatedUserEntity, UserDto.class);
  }

  @Override
  @Transactional
  public void deleteUser(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId)
                                          .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_WITH_ID_NOT_FOUND.toDisplayString()));
    userRepository.delete(userEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> getAllUsers() {
    return userRepository.findAll()
            .stream()
            .map(userEntity -> mapper.map(userEntity, UserDto.class))
            .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> getAllUsers(int page, int limit) {
    Pageable pageable = PageRequest.of(page, limit);

    return userRepository.findAll(pageable)
            .stream()
            .map(userEntity -> mapper.map(userEntity, UserDto.class))
            .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(email)
                                          .orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.USER_WITH_EMAIL_NOT_FOUND.toDisplayString()));

    return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), userEntity.isEnabled(), true, true, true, Collections.emptyList());
  }

  @Override
  @Transactional
  public void verifyEmailToken(String tokenString) {
    // check if token exists
    TokenEntity tokenEntity = tokenRepository.findEmailVerificationToken(tokenString)
                                             .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.TOKEN_NOT_FOUND.toDisplayString()));

    // check if token is expired
    if (tokenEntity.isExpired()) {
      throw new EmailVerificationTokenExpiredException();
    }

    // verify registration
    UserEntity userEntity = tokenEntity.getUser();
    userEntity.setEnabled(true);
    userRepository.save(userEntity);

    tokenRepository.delete(tokenEntity);
  }

  @Override
  @Transactional
  public void changePassword(UserEntity userEntity, String newPassword) {
    userEntity.setEncryptedPassword(passwordEncoder.encode(newPassword));
    userRepository.save(userEntity);
  }

  private void checkIfUserAlreadyExists(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new UserAlreadyExistsException();
    }
  }

}
