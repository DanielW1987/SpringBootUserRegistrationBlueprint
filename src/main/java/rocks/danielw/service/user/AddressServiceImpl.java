package rocks.danielw.service.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.danielw.model.entities.AddressEntity;
import rocks.danielw.model.entities.UserEntity;
import rocks.danielw.model.exceptions.ErrorMessages;
import rocks.danielw.model.exceptions.ResourceNotFoundException;
import rocks.danielw.model.repositories.AddressRepository;
import rocks.danielw.model.repositories.UserRepository;
import rocks.danielw.web.dto.AddressDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

  private final UserRepository    userRepository;
  private final AddressRepository addressRepository;
  private final ModelMapper       mapper;

  @Autowired
  public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
    this.userRepository    = userRepository;
    this.addressRepository = addressRepository;
    this.mapper            = new ModelMapper();
  }

  @Override
  @Transactional(readOnly = true)
  public List<AddressDto> getAddresses(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId)
                                          .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_WITH_ID_NOT_FOUND.toDisplayString()));

    return userEntity.getAddresses().stream()
                                    .map(addressEntity -> mapper.map(addressEntity, AddressDto.class))
                                    .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public AddressDto getAddressByAddressId(String addressId) {
    AddressEntity addressEntity = addressRepository.findByAddressId(addressId)
                                                   .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ADDRESS_WITH_ID_NOT_FOUND.toDisplayString()));

    return mapper.map(addressEntity, AddressDto.class);
  }

}
