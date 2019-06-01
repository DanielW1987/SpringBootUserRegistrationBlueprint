package rocks.danielw.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class AddressDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private long id;
  private String addressId;
  private String city;
  private String country;
  private String streetName;
  private String postalCode;
  private String type;
  private UserDto userDetails;

}
