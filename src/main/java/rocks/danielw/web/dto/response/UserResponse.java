package rocks.danielw.web.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
	
  private String userId;
  private String firstName;
  private String lastName;
  private String email;
  private boolean enabled;
  private List<AddressResponse> addresses;

  public String getName() {
    return String.join(" ", firstName, lastName);
  }
	
}
