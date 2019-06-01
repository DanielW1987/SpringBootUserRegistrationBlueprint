package rocks.danielw.web.controller.web.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rocks.danielw.config.constants.Endpoints;
import rocks.danielw.config.constants.ViewNames;

@Controller
@RequestMapping(Endpoints.LOGIN)
public class LoginController {

  @GetMapping
  public String showLoginForm() {
    return ViewNames.LOGIN;
  }

}
