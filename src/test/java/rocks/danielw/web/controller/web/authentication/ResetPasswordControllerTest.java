package rocks.danielw.web.controller.web.authentication;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rocks.danielw.security.WebSecurity;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ResetPasswordController.class)
@Import(WebSecurity.class)
class ResetPasswordControllerTest {

  // ToDo DanielW: Implement this

}
