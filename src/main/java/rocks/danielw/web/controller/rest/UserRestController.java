package rocks.danielw.web.controller.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rocks.danielw.model.exceptions.ErrorMessages;
import rocks.danielw.model.exceptions.ValidationException;
import rocks.danielw.service.user.AddressService;
import rocks.danielw.service.user.UserService;
import rocks.danielw.web.dto.AddressDto;
import rocks.danielw.web.dto.UserDto;
import rocks.danielw.web.dto.request.CreateUserRequest;
import rocks.danielw.web.dto.request.UpdateUserRequest;
import rocks.danielw.web.dto.response.AddressResponse;
import rocks.danielw.web.dto.response.UserResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/rest/v1/users")
public class UserRestController {

  private final UserService    userService;
  private final AddressService addressService;
  private final ModelMapper    mapper;

  @Autowired
  public UserRestController(UserService userService, AddressService addressService) {
    this.userService    = userService;
		this.addressService = addressService;
    this.mapper         = new ModelMapper();
	}

  @GetMapping(path="/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public Resource<UserResponse> getUser(@PathVariable String id) {
    UserDto      userDto      = userService.getUserByUserId(id);
    UserResponse userResponse = mapUserDtoWithHateosSupport(userDto);

    return new Resource<>(userResponse);
  }
	
  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public Resources<UserResponse> getUsers(@RequestParam(name="page", defaultValue="0")  int page,
			                                    @RequestParam(name="limit", defaultValue="5") int limit) {
    List<UserResponse> userResponses = userService.getAllUsers(page, limit)
                                                  .stream()
                                                  .map(this::mapUserDtoWithHateosSupport)
                                                  .collect(Collectors.toList());

    return new Resources<>(userResponses);
	}
	
  @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
               produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @ResponseStatus(HttpStatus.CREATED)
  public Resource<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new ValidationException(ErrorMessages.VALIDATION_ERRORS.toDisplayString(), bindingResult.getFieldErrors());
    }

    UserDto userDto           = mapper.map(request, UserDto.class);
    UserDto createdUser       = userService.createUser(userDto);
    UserResponse userResponse = mapUserDtoWithHateosSupport(createdUser);

    return new Resource<>(userResponse);
  }

  @PutMapping(path="/{id}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
                            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public Resource<UserResponse> updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserRequest request, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new ValidationException(ErrorMessages.VALIDATION_ERRORS.toDisplayString(), bindingResult.getFieldErrors());
    }

    UserDto userDto           = mapper.map(request, UserDto.class);
    UserDto updatedUser       = userService.updateUser(id, userDto);
    UserResponse userResponse = mapUserDtoWithHateosSupport(updatedUser);
		
    return new Resource<>(userResponse);
	}
	
  @DeleteMapping(path="/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Void> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping(path="/{id}/addresses", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public Resources<AddressResponse> getUserAddresses(@PathVariable(name="id") String userId){
    List<AddressDto>      addressDtos      = addressService.getAddresses(userId);
    List<AddressResponse> addressResponses = addressDtos.stream()
                                                        .map(addressDto -> mapAddressDtoWithHateoasSupport(addressDto, userId))
                                                        .collect(Collectors.toList());

    return new Resources<>(addressResponses);
	}
	
  @GetMapping(path="/{id}/addresses/{addressId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  public Resource<AddressResponse> getUserAddress(@PathVariable String id, @PathVariable String addressId) {
    AddressDto addressDto           = addressService.getAddressByAddressId(addressId);
    AddressResponse addressResponse = mapAddressDtoWithHateoasSupport(addressDto, id);

    return new Resource<>(addressResponse);
	}

  private UserResponse mapUserDtoWithHateosSupport(UserDto userDto) {
    UserResponse userResponse = mapper.map(userDto, UserResponse.class);

    userResponse.getAddresses().forEach(addressResponse -> {
      addressResponse.add(linkTo(methodOn(UserRestController.class).getUserAddress(userDto.getUserId(),addressResponse.getAddressId())).withSelfRel());
      addressResponse.add(linkTo(methodOn(UserRestController.class).getUserAddresses(userDto.getUserId())).withRel("addresses"));
      addressResponse.add(linkTo(methodOn(UserRestController.class).getUser(userDto.getUserId())).withRel("user"));
    });

    return userResponse;
  }

  private AddressResponse mapAddressDtoWithHateoasSupport(AddressDto addressDto, String userId) {
    AddressResponse addressResponse = mapper.map(addressDto, AddressResponse.class);
    addressResponse.add(linkTo(methodOn(UserRestController.class).getUserAddress(userId, addressDto.getAddressId())).withSelfRel());
    addressResponse.add(linkTo(methodOn(UserRestController.class).getUserAddresses(userId)).withRel("addresses"));
    addressResponse.add(linkTo(methodOn(UserRestController.class).getUser(userId)).withRel("user"));

    return addressResponse;
  }
}
