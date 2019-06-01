package rocks.danielw.web.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

@NoArgsConstructor
@Getter
@Setter
public class AddressResponse extends ResourceSupport {

  private String addressId;
  private String city;
  private String country;
  private String streetName;
  private String postalCode;
  private String type;
	
}
