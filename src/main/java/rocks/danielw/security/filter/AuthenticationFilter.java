package rocks.danielw.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rocks.danielw.config.SpringApplicationContext;
import rocks.danielw.security.ExpirationTime;
import rocks.danielw.service.user.UserService;
import rocks.danielw.utils.JwtsUtils;
import rocks.danielw.web.dto.UserDto;
import rocks.danielw.web.dto.request.UserLoginRequest;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  AuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      // Parse request from JSON into UserLoginRequest
      UserLoginRequest credentials = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);

      // Create authentication token
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(),
                                                                                                        credentials.getPassword(),
                                                                                                        Collections.emptyList());

      // Do the authentication with authenticationManager
      return authenticationManager.authenticate(authenticationToken);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication authResult) {

    String username = ((User) authResult.getPrincipal()).getUsername(); // email is username
    String token = Jwts.builder()
                       .setSubject(username)
                       .setExpiration(new Date(System.currentTimeMillis() + ExpirationTime.TEN_DAYS.toMillis()))
                       .signWith(SignatureAlgorithm.HS512, JwtsUtils.getTokenSecret())
                       .compact();

    UserService userService = SpringApplicationContext.getBean("userServiceImpl", UserService.class);
    //noinspection OptionalGetWithoutIsPresent; it's safe to do this in this way
    UserDto userDto = userService.getUserByEmail(username).get();

    response.addHeader(JwtsUtils.HEADER_STRING, JwtsUtils.TOKEN_PREFIX + token);
    response.addHeader("Public user id", userDto.getUserId());

  }
}
