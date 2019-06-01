package rocks.danielw.web.controller.web.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import rocks.danielw.config.constants.Endpoints;
import rocks.danielw.config.constants.MessageKeys;
import rocks.danielw.config.constants.ViewNames;
import rocks.danielw.security.password.OnResetPasswordRequestCompleteEvent;
import rocks.danielw.service.user.UserService;
import rocks.danielw.web.dto.UserDto;
import rocks.danielw.web.dto.request.ForgotPasswordRequest;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(Endpoints.FORGOT_PASSWORD)
public class ForgotPasswordController {

  private static final String FORM_DTO = "forgotPasswordRequest";

  private final ApplicationEventPublisher eventPublisher;
  private final UserService               userService;
  private final MessageSource             messages;

  @Autowired
  public ForgotPasswordController(UserService userService, ApplicationEventPublisher eventPublisher,
                                  @Qualifier("messageSource") MessageSource messages) {
    this.userService    = userService;
    this.eventPublisher = eventPublisher;
    this.messages       = messages;
  }

  @ModelAttribute(FORM_DTO)
  private ForgotPasswordRequest forgotPasswordRequest() {
    return new ForgotPasswordRequest();
  }

  @GetMapping
  public ModelAndView showForgotPasswordForm() {
    return new ModelAndView(ViewNames.FORGOT_PASSWORD);
  }

  @PostMapping
  public ModelAndView requestPasswordReset(@Valid @ModelAttribute ForgotPasswordRequest forgotPasswordRequest, BindingResult bindingResult, WebRequest request) {
    Optional<UserDto> userDto = userService.getUserByEmail(forgotPasswordRequest.getEmail());

    if (! userDto.isPresent()) {
      bindingResult.rejectValue("email", "userDoesNotExist", messages.getMessage(MessageKeys.ForgotPassword.USER_DOES_NOT_EXIST, null, request.getLocale()));
      return new ModelAndView(ViewNames.FORGOT_PASSWORD);
    }

    eventPublisher.publishEvent(new OnResetPasswordRequestCompleteEvent(this, userDto.get()));

    Map<String, Object> viewModel = new HashMap<>();
    viewModel.put("successMessage", messages.getMessage(MessageKeys.ForgotPassword.SUCCESS, null, request.getLocale()));
    viewModel.put("forgotPasswordRequest", new ForgotPasswordRequest()); // to clear the form

    return new ModelAndView(ViewNames.FORGOT_PASSWORD, viewModel);
  }

}
