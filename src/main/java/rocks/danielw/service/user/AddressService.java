package rocks.danielw.service.user;

import rocks.danielw.web.dto.AddressDto;

import java.util.List;

public interface AddressService {

  List<AddressDto> getAddresses(String userId);
  AddressDto getAddressByAddressId(String addressId);
	
}
