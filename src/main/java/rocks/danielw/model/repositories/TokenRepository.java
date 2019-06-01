package rocks.danielw.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rocks.danielw.model.entities.TokenEntity;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

  Optional<TokenEntity> findByTokenString(String tokenString);

  /*
   * Note that it is not necessary to use SQL Native query here. Works also with method name
   * findByTokenStringAndTokenType(String tokenString, TokenType tokenType);
   */
  @Query(value = "SELECT * FROM tokens WHERE TOKEN_TYPE = 'EMAIL_VERIFICATION' AND TOKEN_STRING = :tokenString", nativeQuery = true)
  Optional<TokenEntity> findEmailVerificationToken(String tokenString);

  @Query(value = "SELECT * FROM tokens WHERE TOKEN_TYPE = 'PASSWORD_RESET' AND TOKEN_STRING = :tokenString", nativeQuery = true)
  Optional<TokenEntity> findResetPasswordToken(String tokenString);

}
