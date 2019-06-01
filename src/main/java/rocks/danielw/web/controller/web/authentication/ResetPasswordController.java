package rocks.danielw.web.controller.web.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rocks.danielw.config.constants.Endpoints;
import rocks.danielw.config.constants.MessageKeys;
import rocks.danielw.config.constants.ViewNames;
import rocks.danielw.model.entities.TokenEntity;
import rocks.danielw.model.entities.UserEntity;
import rocks.danielw.service.token.TokenService;
import rocks.danielw.service.user.UserService;
import rocks.danielw.web.dto.request.ChangePasswordRequest;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(Endpoints.RESET_PASSWORD)
public class ResetPasswordController {

  private static final String FORM_DTO = "changePasswordRequest";

  private final UserService userService;
  private final TokenService tokenService;
  private final MessageSource messages;

  @Autowired
  public ResetPasswordController(UserService userService, TokenService tokenService, @Qualifier("messageSource") MessageSource messages) {
    this.userService  = userService;
    this.tokenService = tokenService;
    this.messages     = messages;
  }

  @ModelAttribute(FORM_DTO)
  private ChangePasswordRequest changePasswordRequest() {
    return new ChangePasswordRequest();
  }

  @GetMapping
  public ModelAndView showResetPasswordForm(@RequestParam(value="token") String tokenString, WebRequest request, Model model, RedirectAttributes redirectAttributes) {
    Optional<TokenEntity> tokenEntity = tokenService.getResetPasswordTokenByTokenString(tokenString);

    if (! tokenEntity.isPresent()) {
      redirectAttributes.addFlashAttribute("resetPasswordError", messages.getMessage(MessageKeys.ForgotPassword.TOKEN_DOES_NOT_EXIST, null, request.getLocale()));
      return new ModelAndView("redirect:" + Endpoints.FORGOT_PASSWORD);
    }
    else if (tokenEntity.get().isExpired()) {
      redirectAttributes.addFlashAttribute("resetPasswordError", messages.getMessage(MessageKeys.ForgotPassword.TOKEN_IS_EXPIRED, null, request.getLocale()));
      return new ModelAndView("redirect:" + Endpoints.FORGOT_PASSWORD);
    }
    else {
      // everything is OK, set token as hidden input field
      if (model.asMap().containsKey(FORM_DTO)) {
        ((ChangePasswordRequest) model.asMap().get(FORM_DTO)).setTokenString(tokenString);
      }
      else {
        ChangePasswordRequest changePasswordRequest = changePasswordRequest();
        changePasswordRequest.setTokenString(tokenString);
        model.addAttribute(changePasswordRequest);
      }
    }

    return new ModelAndView(ViewNames.RESET_PASSWORD);
  }

  @PostMapping
  public ModelAndView resetPassword(@Valid @ModelAttribute ChangePasswordRequest changePasswordRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    // show reset password form if there are validation errors
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute(BindingResult.class.getName() + "." + FORM_DTO, bindingResult);
      redirectAttributes.addFlashAttribute(FORM_DTO, changePasswordRequest);
      return new ModelAndView("redirect:" + Endpoints.RESET_PASSWORD + "?token=" + changePasswordRequest.getTokenString());
    }

    changePassword(changePasswordRequest);

    return new ModelAndView("redirect:" + Endpoints.LOGIN + "?resetSuccess");
  }

  private void changePassword(ChangePasswordRequest changePasswordRequest) {
    // 1. get user from token
    Optional<TokenEntity> tokenEntity = tokenService.getResetPasswordTokenByTokenString(changePasswordRequest.getTokenString());
    @SuppressWarnings("OptionalGetWithoutIsPresent") // safe here, because we have validation in method showResetPasswordForm()
    UserEntity userEntity = tokenEntity.get().getUser();

    // 2. set new password
    userService.changePassword(userEntity, changePasswordRequest.getPassword());

    // 3. remove token from database
    tokenService.deleteToken(tokenEntity.get());
  }

}
