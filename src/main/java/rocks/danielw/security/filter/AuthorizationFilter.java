package rocks.danielw.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import rocks.danielw.utils.JwtsUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

class AuthorizationFilter extends BasicAuthenticationFilter {

  AuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

    // read json web token sent by client from http header
    String token = request.getHeader(JwtsUtils.HEADER_STRING);

    // return if token is null or starts not with the expected prefix
    if (token == null || ! token.startsWith(JwtsUtils.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    // create and set authentication object
    UsernamePasswordAuthenticationToken authentication = getAuthentication(request, token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {
    // remove token prefix
    token = token.replaceAll(JwtsUtils.TOKEN_PREFIX, "");

    // decrypt token
    String user = JwtsUtils.getClaims(token).getSubject();

    if (user != null) {
      return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }

    return null;
  }
}
