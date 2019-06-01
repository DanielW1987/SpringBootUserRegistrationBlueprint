package rocks.danielw.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private long id;
  private String userId;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private boolean enabled;
  private List<AddressDto> addresses = new ArrayList<>();
	
}
