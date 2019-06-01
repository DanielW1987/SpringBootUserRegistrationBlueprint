package rocks.danielw.model.email;

import rocks.danielw.config.constants.Endpoints;
import rocks.danielw.config.constants.ViewNames;

import java.util.HashMap;
import java.util.Map;

public final class ForgotPasswordEmail extends AbstractEmail {

  private final String receiver;
  private final String firstName;
  private final String resetPasswordToken;

  private static final String  RESET_PASSWORD_URL = Endpoints.RESET_PASSWORD + "?token=%s";
  private static final String  SUBJECT            = "Your password reset request";

  public ForgotPasswordEmail(String receiver, String firstName, String resetPasswordToken) {
    this.receiver           = receiver;
    this.firstName          = firstName;
    this.resetPasswordToken = resetPasswordToken;
  }

  @Override
  public String getRecipient() {
    return receiver;
  }

  @Override
  public String getSubject() {
    return SUBJECT;
  }

  @Override
  public Map<String, Object> getViewModel() {
    Map<String, Object> viewModel = new HashMap<>();
    viewModel.put("firstName", firstName);
    viewModel.put("resetPasswordURL", createResetPasswordURL());

    return viewModel;
  }

  @Override
  public String getTemplateName() {
    return ViewNames.EmailTemplates.FORGOT_PASSWORD;
  }

  private String createResetPasswordURL() {
    return String.format(HOST + ":" + PORT + RESET_PASSWORD_URL, resetPasswordToken);
  }
}
