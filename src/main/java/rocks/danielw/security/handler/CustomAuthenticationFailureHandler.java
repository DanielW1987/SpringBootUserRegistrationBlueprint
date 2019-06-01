package rocks.danielw.security.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import rocks.danielw.config.constants.Endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
    String redirectURL = exception instanceof DisabledException ? Endpoints.LOGIN + "?disabled" : Endpoints.LOGIN + "?badCredentials";

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.sendRedirect(redirectURL);
  }
}
