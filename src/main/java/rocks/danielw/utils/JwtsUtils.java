package rocks.danielw.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import rocks.danielw.config.AppProperties;
import rocks.danielw.config.SpringApplicationContext;
import rocks.danielw.security.ExpirationTime;

import java.util.Date;

public class JwtsUtils {

  public  static final String TOKEN_PREFIX  = "Bearer ";
  public  static final String HEADER_STRING = "Authorization";
  private static       String TOKEN_SECRET;

  private JwtsUtils() {
    // prevent object creation
  }

  public static String generateToken(String subject, ExpirationTime expirationTime) {
    return Jwts.builder()
               .setSubject(subject)
               .setExpiration(new Date(System.currentTimeMillis() + expirationTime.toMillis()))
               .signWith(SignatureAlgorithm.HS512, getTokenSecret())
               .compact();
  }

  public static Claims getClaims(String token) {
    return Jwts.parser()
               .setSigningKey(getTokenSecret())
               .parseClaimsJws(token)
               .getBody();
  }

  public static String getTokenSecret() {
    if (TOKEN_SECRET == null) {
      AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
      TOKEN_SECRET = appProperties.getTokenSecret();
    }

    return TOKEN_SECRET;
  }

}
