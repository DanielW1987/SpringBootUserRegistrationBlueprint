package rocks.danielw.web.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rocks.danielw.web.validation.FieldsValueMatch;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldsValueMatch.List({
  @FieldsValueMatch(field = "password", fieldMatch = "verifyPassword", message = "The Passwords must match")
})
@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordRequest {

  private String tokenString;

  @NotBlank
  @Size(min=8, message="Password length must be at least 8 characters")
  private String password;

  @NotBlank
  @Size(min=8, message="Password length must be at least 8 characters")
  private String verifyPassword;

}
