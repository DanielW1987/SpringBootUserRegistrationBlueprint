package rocks.danielw.web.controller.web.authentication;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import rocks.danielw.config.constants.Endpoints;
import rocks.danielw.config.constants.MessageKeys;
import rocks.danielw.config.constants.ViewNames;
import rocks.danielw.model.exceptions.EmailVerificationTokenExpiredException;
import rocks.danielw.model.exceptions.ResourceNotFoundException;
import rocks.danielw.model.exceptions.UserAlreadyExistsException;
import rocks.danielw.security.registration.OnRegistrationCompleteEvent;
import rocks.danielw.service.token.TokenService;
import rocks.danielw.service.user.UserService;
import rocks.danielw.web.dto.UserDto;
import rocks.danielw.web.dto.request.RegisterUserRequest;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RegistrationController {

  private static final String FORM_DTO = "registerUserRequest";

  private final ApplicationEventPublisher eventPublisher;
  private final UserService               userService;
  private final TokenService              tokenService;
  private final MessageSource             messages;
  private final ModelMapper               mapper;

  @Autowired
  public RegistrationController(UserService userService, TokenService tokenService, ApplicationEventPublisher eventPublisher,
                                @Qualifier("messageSource") MessageSource messages) {
    this.userService    = userService;
    this.tokenService   = tokenService;
    this.eventPublisher = eventPublisher;
    this.messages       = messages;
    this.mapper         = new ModelMapper();
  }

  @ModelAttribute(FORM_DTO)
  private RegisterUserRequest registerUserRequest() {
    return new RegisterUserRequest();
  }

  @GetMapping(Endpoints.REGISTER)
  public ModelAndView showRegistrationForm() {
    return new ModelAndView(ViewNames.REGISTER);
  }

  @PostMapping(Endpoints.REGISTER)
  public ModelAndView registerUserAccount(@Valid @ModelAttribute RegisterUserRequest registerUserRequest, BindingResult bindingResult, WebRequest request) {
    // show registration form if there are validation errors
    if (bindingResult.hasErrors()) {
      return new ModelAndView(ViewNames.REGISTER);
    }

    // create user
    UserDto createdUserDto;
    UserDto userDto = mapper.map(registerUserRequest, UserDto.class);

    try{
      createdUserDto = userService.createUser(userDto);
    }
    catch (UserAlreadyExistsException e) {
      bindingResult.rejectValue("email", "userAlreadyExists", e.getMessage());
      return new ModelAndView(ViewNames.REGISTER); // show registration form with validation errors
    }

    eventPublisher.publishEvent(new OnRegistrationCompleteEvent(this, createdUserDto));

    Map<String, Object> viewModel = new HashMap<>();
    viewModel.put("successMessage", messages.getMessage(MessageKeys.Registration.SUCCESS, null, request.getLocale()));
    viewModel.put("registerUserRequest", new RegisterUserRequest()); // to clear the register form

    return new ModelAndView(ViewNames.REGISTER, viewModel);
  }

  @GetMapping(Endpoints.REGISTRATION_VERIFICATION)
  public ModelAndView verifyRegistration(@RequestParam(value="token") String tokenString) {
    String param = "verificationSuccess";

    try{
      userService.verifyEmailToken(tokenString);
    }
    catch (EmailVerificationTokenExpiredException e) {
      param = "tokenExpired&token=" + tokenString;
    }
    catch (ResourceNotFoundException e) {
      param = "tokenNotFound";
    }

    return new ModelAndView("redirect:" + Endpoints.LOGIN + "?" + param);
  }

  @GetMapping(Endpoints.RESEND_VERIFICATION_TOKEN)
  public ModelAndView resendEmailVerificationToken(@RequestParam(value="token") String tokenString) {
    String param = "resendVerificationTokenSuccess";

    try{
      tokenService.resendExpiredEmailVerificationToken(tokenString);
    }
    catch (ResourceNotFoundException e) {
      param = "tokenNotFound";
    }

    return new ModelAndView("redirect:" + Endpoints.LOGIN + "?" + param);
  }

}
