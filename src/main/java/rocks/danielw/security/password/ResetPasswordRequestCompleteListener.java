package rocks.danielw.security.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import rocks.danielw.model.email.ForgotPasswordEmail;
import rocks.danielw.service.email.EmailService;
import rocks.danielw.service.token.TokenService;

@Component
public class ResetPasswordRequestCompleteListener implements ApplicationListener<OnResetPasswordRequestCompleteEvent> {

  private EmailService emailService;
  private final TokenService tokenService;

  @Autowired
  public ResetPasswordRequestCompleteListener(EmailService emailService, TokenService tokenService) {
    this.tokenService = tokenService;
    this.emailService = emailService;
  }

  @Override
  public void onApplicationEvent(OnResetPasswordRequestCompleteEvent event) {
    sendResetPasswordEmail(event);
  }

  private void sendResetPasswordEmail(OnResetPasswordRequestCompleteEvent event) {
    String token = tokenService.createResetPasswordToken(event.getUserDto().getUserId());
    emailService.sendEmail(new ForgotPasswordEmail(event.getUserDto().getEmail(), event.getUserDto().getFirstName(), token));
  }
}
