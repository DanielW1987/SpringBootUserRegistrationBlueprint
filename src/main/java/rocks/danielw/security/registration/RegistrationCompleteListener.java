package rocks.danielw.security.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import rocks.danielw.model.email.RegistrationVerificationEmail;
import rocks.danielw.service.email.EmailService;
import rocks.danielw.service.token.TokenService;
import rocks.danielw.web.dto.UserDto;

@Component
public class RegistrationCompleteListener implements ApplicationListener<OnRegistrationCompleteEvent> {

  private EmailService emailService;
  private TokenService tokenService;

  @Autowired
  public RegistrationCompleteListener(EmailService emailService, TokenService tokenService) {
    this.tokenService = tokenService;
    this.emailService = emailService;
  }

  @Override
  public void onApplicationEvent(OnRegistrationCompleteEvent event) {
    sendConfirmationEmail(event);
  }

  private void sendConfirmationEmail(OnRegistrationCompleteEvent event) {
    UserDto userDto = event.getUserDto();
    String token    = tokenService.createEmailVerificationToken(userDto.getUserId());

    emailService.sendEmail(new RegistrationVerificationEmail(userDto.getEmail(), token));
  }
}
